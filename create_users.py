import sys
from random import randint
import calendar
import datetime


NUM_USERS_TO_CREATE = 10000
NUM_POSTS_PER_USER = 5

WORK_OFFLINE_DATA = True


# monthEnum = {v: k for k,v in enumerate(calendar.month_abbr)}
ballGamesList = ['Soccer', 'Football', 'Basketball', 'Foosball', 'Tennis']
# ballGamesEnum = {v: k for k,v in enumerate(ballGamesList)}
videoGamesList = ['Overwatch', 'League Of Legends', 'Call Of Duty', 'Fortnite', 'Mario Kart']
# videoGamesEnum = {v: k for k,v in enumerate(videoGamesList)}
boardGamesList = ['Catan', 'Clue', 'Risk', 'Talisman', 'Monopoly']
# boardGamesEnum = {v: k for k,v in enumerate(boardGamesList)}
workoutList = ['Spinning', 'Zumba', 'Cycling', 'Yoga', 'Running']
# workoutEnum = {v: k for k,v in enumerate(workoutList)}
citiesList = ['Haifa', 'Tel-Aviv', 'Jerusalem', 'Netanya', 'Beer-sheva', 'Eilat', 'Nahariya', 'Qiryat-shemona']
# citiesEnum = {v: k for k,v in enumerate(citiesList)}
categoryList = ['Ball Games', 'Video Games', 'Board Games', 'Workout']
# categoryEnum = {v: k for k,v in enumerate(categoryList)}
# gamesByCategoryEnumList = [ballGamesEnum, videoGamesEnum, boardGamesEnum, workoutEnum]
gameListByCategory = [ballGamesList, videoGamesList, boardGamesList, workoutList]


def timeValueToString(time):
    hour = (time - (time % 4))/4
    min = (time % 4)*15
    return '{:02d}'.format(hour) + ':' + '{:02d}'.format(min)

def getPostKey(userNickname, postNum):
    return userNickname + '-' + '{:03d}'.format(postNum)

MIN_DAY = 1
MAX_DAY = 28
MIN_MONTH = 1
MAX_MONTH = 12
MIN_YEAR = 2018
MAX_YEAR = 2019
MIN_TIME = 0
MAX_TIME = 4 * 24
MIN_AGE = 18
MAX_AGE = 60
MIN_CITY = 0
MAX_CITY = len(citiesList) - 1
MIN_CATEGORY = 0
MAX_CATEGORY = len(categoryList) - 1
MIN_GAME = 0
MAX_GAME = 4
MIN_DESIRED_PLAYERS = 2
MAX_DESIRED_PLAYERS = 20

def createMidDicts(year,monthStr,day,timeStr,dict):
    if year     not in dict:
        dict[year] = {}
    if monthStr not in dict[year]:
        dict[year][monthStr] = {}
    if day      not in dict[year][monthStr]:
        dict[year][monthStr][day] = {}
    if timeStr  not in dict[year][monthStr][day]:
        dict[year][monthStr][day][timeStr] = {}

startTime = datetime.datetime.now()
tempTime = startTime
def startTimer():
    startTime = datetime.datetime.now()
    tempTime = startTime

def timePassed(stepStr = ''):
    took = datetime.datetime.now() - tempTime
    print 'took ' + str(took) + ' for ' + stepStr

def timeTotal():
    took = datetime.datetime.now() - startTime
    print 'script took ' + str(took) + ' in total'


def main(argv):
    startTimer()
    from firebase import firebase
    firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)
    usersPath = 'users'
    if WORK_OFFLINE_DATA:
        usersPath = 'offlineUsers'


    users = {}
    allPosts = {}
    for userNum in range(NUM_USERS_TO_CREATE):

        nickname = '{:06d}'.format(userNum)
        users[nickname] = {}
        user = users[nickname]
        user['user_name'] = nickname + '@a.com'
        user['city'] = citiesList[randint(MIN_CITY,MAX_CITY)]
        user['age'] = randint(MIN_AGE,MAX_AGE)

        # create posts
        user['created'] = {}
        created = user['created']
        for numPost in range(NUM_POSTS_PER_USER):
            year = randint(MIN_YEAR,MAX_YEAR)
            month = randint(MIN_MONTH,MAX_MONTH)
            monthStr = calendar.month_abbr[month]
            day = '{:02d}'.format(randint(MIN_DAY,MAX_DAY))
            time = randint(MIN_TIME,MAX_TIME)
            timeStr = timeValueToString(time)

            createMidDicts(year,monthStr,day,timeStr,created)
            postKey = getPostKey(nickname,numPost)
            created[year][monthStr][day][timeStr][postKey] = {}
            post = created[year][monthStr][day][timeStr][postKey]



            categoryNum = randint(MIN_CATEGORY,MAX_CATEGORY)
            category = categoryList[categoryNum]
            game = gameListByCategory[categoryNum][randint(MIN_GAME,MAX_GAME)]
            desiredPlayers = randint(MIN_DESIRED_PLAYERS,MAX_DESIRED_PLAYERS)
            currentPlayers = randint(MIN_DESIRED_PLAYERS - 1,desiredPlayers - 1)
            description = 'n/a'


            post['category'] = category
            post['game'] = game
            post['currentNumPlayers'] = currentPlayers
            post['desiredNumPlayers'] = desiredPlayers
            post['description'] = description
            post['host_age'] = user['age']
            post['city'] = user['city']
            post['user_name'] = user['user_name']
            post['userID'] = user['user_name']

            #copy post to all posts
            createMidDicts(year,monthStr,day,timeStr,allPosts)
            allPosts[year][monthStr][day][timeStr][postKey] = post

            #copy posts to attending
        user['attending'] = user['created']

    timePassed('creating users and their posts')


    postsPath = 'offlinePosts'
    if not WORK_OFFLINE_DATA:
        postsPath = 'posts'
        for userKey,userData in users.items():
            firebase.put(usersPath,userKey,userData)
            userPosts = userData['attending']
            for year,yearData in userPosts.items():
                for month,monthData in yearData.items():
                    for day,dayData in monthData.items():
                        for time,timeData in dayData.items():
                            for postKey,postData in timeData.items():
                                dateTimePath = str(year) + '/' + month + '/' + day + '/' + time
                                firebase.put(postsPath + '/' + dateTimePath,postKey,postData)
        timePassed('writing to DB')
    else:
        firebase.put('/',postsPath,allPosts)
        firebase.put('/',usersPath,users)
        timePassed('writing to DB')

    timeTotal()

if __name__ == '__main__':
    main(sys.argv)







