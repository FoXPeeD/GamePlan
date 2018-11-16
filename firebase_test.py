import sys
import datetime
import calendar
import numpy as np
from sklearn.neighbors import NearestNeighbors

TIME_WEIGHT = 1
DAY_WEIGHT = 1
GAME_WEIGHT = 1
CATEGORY_WEIGHT = 1
CITY_WEIGHT = 1
AGE_WEIGHT = 1
NUM_PLAYERS_WEIGHT = 1

dayOfWeekEnum = {v: k for k,v in enumerate(calendar.month_abbr)}
ballGamesList = ['Soccer', 'Football', 'Basketball', 'Foosball', 'Tennis']
ballGamesEnum = {v: k for k,v in enumerate(ballGamesList)}
boardGamesList = ['Overwatch', 'League Of Legends', 'Call Of Duty', 'Fortnite']
boardGamesEnum = {v: k for k,v in enumerate(boardGamesList)}
videoGamesList = ['Catan', 'Clue', 'Risk', 'Talisman', 'Monopoly']
videoGamesEnum = {v: k for k,v in enumerate(videoGamesList)}
workoutList = ['Spinning', 'Zumba', 'Bicycling', 'Yoga']
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
    return datetime.date(int(year), int(dayOfWeekEnum[month]), int(day)).weekday()


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
    users = firebase.get('users', None)
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
    # print (usersAvgData)



    #########collect posts data ########
    posts = firebase.get('posts', None)
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
                if (datetime.datetime(year=int(yearKey),month=int(dayOfWeekEnum[monthKey]), day=int(dayKey)) < tomorrow):
                    continue
                for time,timeData in dayData.items():
                    for postKey,postData in timeData.items():
                        #print (postKey)
                        postsInfoList.append({'postId':postKey,
                                              'category':postData['category'],
                                              'game':postData['game'],
                                              # 'dateTime':yearKey + '/' + monthKey + '/' + dayKey + '/' + time,
                                              'year':yearKey,
                                              'month':monthKey,
                                              'day':dayKey,
                                              'time':time,
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

    # print (postsArray)

    ########calculate Nearest Neighbors###########
    neigh = NearestNeighbors(algorithm='auto', metric='minkowski')
    neigh.fit(postsArray)

    for userId,avgPost in usersAvgData.items():
        recommendPath = 'users/' + userId + '/recommended'
        firebase.delete(recommendPath,None)

        recommendedPosts = {}
        nearest = neigh.kneighbors([avgPost], 1, return_distance=False)
        print(nearest)
        indexes = []
        for index in nearest:
            indexes.append(int(index))
        #
        #remove posts user is already attending

        for index in indexes:
            if isUserAttending(index,postsInfoList[index]['postId'],userId,usersRawData):
                continue
            print (postData['userID'])
            post = postsInfoList[index]
            if post['year']     not in recommendedPosts:
                recommendedPosts[post['year']] = {}
            if post['month']  not in recommendedPosts[post['year']]:
                recommendedPosts[post['year']][post['month']] = {}
            if post['day']    not in recommendedPosts[post['year']][post['month']]:
                recommendedPosts[post['year']][post['month']][post['day']] = {}
            if post['time']   not in recommendedPosts[post['year']][post['month']][post['day']]:
                recommendedPosts[post['year']][post['month']][post['day']][post['time']] = {}

            recommendedPosts[post['year']][post['month']][post['day']][post['time']][post['postId']] = {
                'category':postData['category'],
                'game':postData['game'],
                'userID':postData['userID']}

        # print recommendedPosts
        firebase.put('users/' + userId,'recommended',recommendedPosts)

if __name__ == '__main__':
	main(sys.argv)






