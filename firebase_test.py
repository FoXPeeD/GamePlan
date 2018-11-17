import sys
import datetime
import calendar
import numpy as np
from sklearn.neighbors import NearestNeighbors

WORK_OFFLINE_DATA = True

NUM_OF_KNEIGHBORS = 2
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
videoGamesList = ['Overwatch', 'League Of Legends', 'Call Of Duty', 'Fortnite', 'Mario Cart']
videoGamesEnum = {v: k for k,v in enumerate(videoGamesList)}
boardGamesList = ['Catan', 'Clue', 'Risk', 'Talisman', 'Monopoly']
boardGamesEnum = {v: k for k,v in enumerate(boardGamesList)}
workoutList = ['Spinning', 'Zumba', 'Bicycling', 'Yoga', 'Running']
workoutEnum = {v: k for k,v in enumerate(workoutList)}
citiesList = ['Haifa', 'Tel-Aviv', 'Jerusalem', 'Netanya', 'Beer-sheva', 'Eilat', 'Nahariya', 'Qiryat-shemona']
citiesEnum = {v: k for k,v in enumerate(citiesList)}
categoryList = ['Ball Games', 'Video Games', 'Board Games', 'Workout']
categoryEnum = {v: k for k,v in enumerate(categoryList)}
gamesByCategoryEnumList = [ballGamesEnum, videoGamesEnum, boardGamesEnum, workoutEnum]

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


def main(argv):

##import DB from file
    # import json
    # with open("gameplan-1312c-export.json", 'r') as f:
    #     firebase = json.load(f)
    # users = firebase["users"]

##import DB from firebase
    from firebase import firebase
    firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)

    #########agregate users data ########
    usersPath = 'users'
    if WORK_OFFLINE_DATA:
        usersPath = 'offlineUsers'
    users = firebase.get(usersPath, None)
    print ('got users')
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
    print ('finished processing users')



    #########collect posts data ########
    postsPath = 'posts'
    if WORK_OFFLINE_DATA:
        postsPath = 'offlinePosts'
    posts = firebase.get(postsPath, None)
    print ('got posts')
    postsInfoList = []
    postsArray = []

    now = datetime.datetime.now()
    tomorrow = datetime.datetime.today() + datetime.timedelta(days=1)
    tYear = tomorrow.year
    tMonth = tomorrow.month
    tDay = tomorrow.day
    # print(tYear)
    # print(calendar.month_abbr[tMonth])
    # print(tDay)

    for yearKey,yearData in posts.items():
        for monthKey,monthData in yearData.items():
            for dayKey,dayData in monthData.items():
                if (datetime.datetime(year=int(yearKey),month=int(monthEnum[monthKey]), day=int(dayKey)) < tomorrow):
                    continue
                for time,timeData in dayData.items():
                    for postKey,postData in timeData.items():
                        if(postData['desiredNumPlayers'] == postData['currentNumPlayers']):
                            continue
                        # print (postKey)
                        postsInfoList.append({'postId':postKey,
                                              'category':postData['category'],
                                              'game':postData['game'],
                                              # 'dateTime':yearKey + '/' + monthKey + '/' + dayKey + '/' + time,
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
                        #print (year + '/' + month + '/' + day + '/' + time)
                        categoryNum = categoryToValue(postData['category'])
                        postsArray.append([dayOfWeekToValue(yearKey,monthKey,dayKey),
                                              timeToValue(time),
                                              postData['host_age'],
                                              postData['desiredNumPlayers'],
                                              categoryNum,
                                              gameToValue(postData['game'],categoryNum),
                                              cityToValue(postData['city'])])
                        print(postsInfoList)

    # print (postsArray)
    print ('finished processing posts')

    ########calculate Nearest Neighbors###########
    neigh = NearestNeighbors(algorithm='auto', metric='minkowski')
    print ('training...')
    neigh.fit(postsArray)


    print ('finding knn for each user...')
    for userId,avgPost in usersAvgData.items():
        recommendPath = 'users/' + userId + '/recommended'
        firebase.delete(recommendPath,None)

        recommendedPosts = {}
        nearest = neigh.kneighbors([avgPost], NUM_OF_KNEIGHBORS, return_distance=False)
        print('for user ' + str(userId) + ': ' + str(nearest))
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

        # print recommendedPosts
        firebase.put(usersPath + '/' + userId,'recommended',recommendedPosts)

    print ('done!')

if __name__ == '__main__':
	main(sys.argv)






