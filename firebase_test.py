import sys
import datetime
import calendar

def timeToValue(time):
    list = time.split(':')
    return (int(float(list[0]))*4) + (int(float(list[1]))/15)

def timeValueToString(time):
    hour = (time - (time % 4))/4
    min = (time % 4)*15
    return '{:02d}'.format(hour) + ':' + '{:02d}'.format(min)

def dayOfWeekToValue(year,month,day):
    d = {v: k for k,v in enumerate(calendar.month_abbr)}
    return datetime.date(int(year), int(d[month]), int(day)).weekday()

def AddPostToHistogram(users,year,month,day,time,post,userid):
    users[userid]['timeList'].append(timeToValue(time))
    users[userid]['dayOfWeekList'].append(dayOfWeekToValue(year,month,day))

def gameToValue(game):
    return 'TODO'

def categoryToValue(category)
    return 'TODO'

def cityToValue(city)
    return 'TODO'

#age
#num players



def main(argv):

#import DB from file
    # import json
    # with open("gameplan-1312c-export.json", 'r') as f:
    #     firebase = json.load(f)
    # users = firebase["users"]

#import DB from firebase
    from firebase import firebase
    firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)
    users = firebase.get('users', None)


    usersRawData = {}
    for userId,userData in users.items():
        usersRawData[userId] = {}
        usersRawData[userId]['timeList'] = []
        usersRawData[userId]['dayOfWeekList'] = []
        attendingPosts = userData["attending"]
        for year,yearData in attendingPosts.items():
            for month,monthData in yearData.items():
                for day,dayData in monthData.items():
                    for time,timeData in dayData.items():
                        for postKey,postData in timeData.items():
                            print ("*post*")
                            print(postKey)
                            AddPostToHistogram(usersRawData,year,month,day,time,postData,userId)
    print (usersRawData)













if __name__ == '__main__':
	main(sys.argv)






