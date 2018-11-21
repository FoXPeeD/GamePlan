from __future__ import print_function
import socket
import sys
import datetime
import calendar
import numpy as np
from sklearn.neighbors import NearestNeighbors


WORK_OFFLINE_DATA = True
ALGORITHM = 'kd_tree'
NUM_OF_KNEIGHBORS = 5
TIMES_TO_RUN = 5

TIME_WEIGHT = 1
DAY_WEIGHT = 1
GAME_WEIGHT = 10
CATEGORY_WEIGHT = 100
CITY_WEIGHT = 100
AGE_WEIGHT = 1
NUM_PLAYERS_WEIGHT = 1

monthEnum = {v: k for k,v in enumerate(calendar.month_abbr)}
ballGamesList = ['Soccer', 'Football', 'Basketball', 'Foosball', 'Tennis']
ballGamesEnum = {v: k for k,v in enumerate(ballGamesList)}
videoGamesList = ['Overwatch', 'League Of Legends', 'Call Of Duty', 'Fortnite', 'Mario Kart']
videoGamesEnum = {v: k for k,v in enumerate(videoGamesList)}
boardGamesList = ['Catan', 'Clue', 'Risk', 'Talisman', 'Monopoly']
boardGamesEnum = {v: k for k,v in enumerate(boardGamesList)}
workoutList = ['Spinning', 'Zumba', 'Cycling', 'Yoga', 'Running']
workoutEnum = {v: k for k,v in enumerate(workoutList)}
citiesList = ['Haifa', 'Tel-Aviv', 'Jerusalem', 'Netanya', 'Beer-sheva', 'Eilat', 'Nahariya', 'Qiryat-shemona']
citiesEnum = {v: k for k,v in enumerate(citiesList)}
categoryList = ['Ball Games', 'Video Games', 'Board Games', 'Workout']
categoryEnum = {v: k for k,v in enumerate(categoryList)}
gamesByCategoryEnumList = [ballGamesEnum, videoGamesEnum, boardGamesEnum, workoutEnum]

def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)

def most_common(lst):
    return max(set(lst), key=lst.count)
def avarage(lst):
    return reduce(lambda x, y: x + y, lst) / len(lst)

def timeToValue(time):
    list = time.split(':')
    return (int(float(list[0]))*4) + (int(float(list[1]))/15)

def timeValueToString(time):
    hour = (time - (time % 4))/4
    min = (time % 4)*15
    return '{:02d}'.format(hour) + ':' + '{:02d}'.format(min)

def dayOfWeekToValue(year,month,day):
    return datetime.date(int(year), int(monthEnum[month]), int(day)).weekday()


def gameToValue(game, categoryNumValue):
    return int(gamesByCategoryEnumList[categoryNumValue][game])

def categoryToValue(category):
    return int(categoryEnum[category])

def cityToValue(city):
    return int(citiesEnum[city])


def AddPostToHistogram(users,year,month,day,time,post,userId,postKey):
    users[userId]['timeList'].append(timeToValue(time))
    users[userId]['dayOfWeekList'].append(dayOfWeekToValue(year,month,day))
    users[userId]['ageList'].append(post['host_age'])
    users[userId]['numPlayersList'].append(post['desiredNumPlayers'])
    categoryNum = categoryToValue(post['category'])
    users[userId]['categoryList'].append(categoryNum)
    users[userId]['gameList'].append(gameToValue(post['game'],categoryNum))
    users[userId]['cityList'].append(cityToValue(post['city']))
    users[userId]['attending'][postKey] = True

def isUserAttending(x,postID, userID, usersRawData):
    if postID in usersRawData[userID]['attending']:
        return True
    else:
        return False


def run(totalNetTime,TotalScriptTime):

    scriptStartTime = datetime.datetime.now()

##import DB from file
    # import json
    # with open("gameplan-1312c-export.json", 'r') as f:
    #     firebase = json.load(f)
    # users = firebase["users"]

##import DB from firebase
    from firebase import firebase
    firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)

    usersPath = 'users'
    if WORK_OFFLINE_DATA:
        usersPath = 'offlineUsers'
    users = firebase.get(usersPath, None)

    postsPath = 'posts'
    if WORK_OFFLINE_DATA:
        postsPath = 'offlinePosts'
    posts = firebase.get(postsPath, None)



    #########agregate users data ########
    # print('found ' + str(len(users)) + ' users')
    usersRawData = {}
    usersAvgData = {}
    for userId,userData in users.items():

        usersRawData[userId] = {}
        usersRawData[userId]['timeList'] = []
        usersRawData[userId]['dayOfWeekList'] = []
        usersRawData[userId]['ageList'] = []
        usersRawData[userId]['numPlayersList'] = []
        usersRawData[userId]['categoryList'] = []
        usersRawData[userId]['gameList'] = []
        usersRawData[userId]['cityList'] = []
        usersRawData[userId]['attending'] = {}

        attendingPosts = userData["attending"]
        for year,yearData in attendingPosts.items():
            for month,monthData in yearData.items():
                for day,dayData in monthData.items():
                    for time,timeData in dayData.items():
                        for postKey,postData in timeData.items():
                            AddPostToHistogram(usersRawData,year,month,day,time,postData,userId,postKey)

        usersAvgData[userId] = []
        usersAvgData[userId].append(most_common(usersRawData[userId]['dayOfWeekList']) * DAY_WEIGHT)
        usersAvgData[userId].append(avarage(usersRawData[userId]['timeList']) * TIME_WEIGHT)
        usersAvgData[userId].append(avarage(usersRawData[userId]['ageList']) * AGE_WEIGHT)
        usersAvgData[userId].append(avarage(usersRawData[userId]['numPlayersList']) * NUM_PLAYERS_WEIGHT)
        usersAvgData[userId].append(most_common(usersRawData[userId]['categoryList']) * CATEGORY_WEIGHT)
        usersAvgData[userId].append(most_common(usersRawData[userId]['gameList']) * GAME_WEIGHT)
        usersAvgData[userId].append(most_common(usersRawData[userId]['cityList']) * CITY_WEIGHT)


    #########collect posts data ########
    netStartTime = datetime.datetime.now()
    numPostsProcessed = 0

    postsInfoList = []
    postsArray = []

    now = datetime.datetime.now()
    tomorrow = datetime.datetime.today() + datetime.timedelta(days=1)
    tYear = tomorrow.year
    tMonth = tomorrow.month
    tDay = tomorrow.day


    for yearKey,yearData in posts.items():
        for monthKey,monthData in yearData.items():
            for dayKey,dayData in monthData.items():
                if (datetime.datetime(year=int(yearKey),month=int(monthEnum[monthKey]), day=int(dayKey)) < tomorrow):
                    continue
                for time,timeData in dayData.items():
                    for postKey,postData in timeData.items():
                        if(postData['desiredNumPlayers'] == postData['currentNumPlayers']):
                            continue
                        numPostsProcessed = numPostsProcessed + 1
                        postsInfoList.append({'postId':postKey,
                                              'category':postData['category'],
                                              'game':postData['game'],
                                              'year':yearKey,
                                              'month':monthKey,
                                              'day':dayKey,
                                              'time':time,
                                              'city':postData['city'],
                                              'description':postData['description'],
                                              'desiredNumPlayers':postData['desiredNumPlayers'],
                                              'currentNumPlayers':postData['currentNumPlayers'],
                                              'user_name':postData['user_name'],
                                              'userID':postData['userID']})
                        categoryNum = categoryToValue(postData['category'])
                        postsArray.append([dayOfWeekToValue(yearKey,monthKey,dayKey),
                                              timeToValue(time),
                                              postData['host_age'],
                                              postData['desiredNumPlayers'],
                                              categoryNum,
                                              gameToValue(postData['game'],categoryNum),
                                              cityToValue(postData['city'])])



    ########calculate Nearest Neighbors###########
    neigh = NearestNeighbors(algorithm=ALGORITHM, metric='minkowski')
    neigh.fit(postsArray)

    for userId,avgPost in usersAvgData.items():
        recommendPath = 'users/' + userId + '/recommended'
        # firebase.delete(recommendPath,None)

        recommendedPosts = {}
        nearest = neigh.kneighbors([avgPost], NUM_OF_KNEIGHBORS, return_distance=False)
        # print('for user ' + str(userId) + ': ' + str(nearest))
        indexes = []
        for index in nearest[0]:
            indexes.append(int(index))
        #
        #remove posts user is already attending

        for index in indexes:
            if isUserAttending(index,postsInfoList[index]['postId'],userId,usersRawData):
                continue

            post = postsInfoList[index]
            if post['year']     not in recommendedPosts:
                recommendedPosts[post['year']] = {}
            if post['month']    not in recommendedPosts[post['year']]:
                recommendedPosts[post['year']][post['month']] = {}
            if post['day']      not in recommendedPosts[post['year']][post['month']]:
                recommendedPosts[post['year']][post['month']][post['day']] = {}
            if post['time']     not in recommendedPosts[post['year']][post['month']][post['day']]:
                recommendedPosts[post['year']][post['month']][post['day']][post['time']] = {}

            recommendedPosts[post['year']][post['month']][post['day']][post['time']][post['postId']] = {
                'category':post['category'],
                'game':post['game'],
                'city':post['city'],
                'description':post['description'],
                'desiredNumPlayers':post['desiredNumPlayers'],
                'currentNumPlayers':post['currentNumPlayers'],
                'user_name':post['user_name'],
                'userID':post['userID']}

        # send recommendedPosts
        if WORK_OFFLINE_DATA:
            users[userId]['recommended'] = recommendedPosts
        else:
            firebase.put(usersPath + '/' + userId,'recommended',recommendedPosts)

    netEndTime = datetime.datetime.now()

    if WORK_OFFLINE_DATA:
        try:
            firebase.put('/',usersPath,users)
        except (socket.error), e:
            eprint('<class "socket.error">: [Errno 10053] An established connection was aborted by the software in your host machine)')
            eprint('trying again')
            firebase.put('/',usersPath,users)

    scriptEndTime = datetime.datetime.now()


    print('all: ' + str(scriptEndTime - scriptStartTime) + ', net: ' + str(netEndTime - netStartTime))
    TotalScriptTime = TotalScriptTime + scriptEndTime - scriptStartTime
    totalNetTime = totalNetTime + netEndTime - netStartTime

def main(argv):

    print('number of recommendations: ' + str(NUM_OF_KNEIGHBORS))
    totalNetTime = datetime.datetime.now() - datetime.datetime.now()
    TotalScriptTime = datetime.datetime.now() - datetime.datetime.now()
    for i in range(TIMES_TO_RUN):
        run(totalNetTime,TotalScriptTime)
    print('Average script time: ' + str(TotalScriptTime/TIMES_TO_RUN) + ', Average net time: ' + str(totalNetTime/TIMES_TO_RUN))

if __name__ == '__main__':
    main(sys.argv)






