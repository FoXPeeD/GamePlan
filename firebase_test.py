import sys
def main(argv):

#import DB from file
    import json
    with open("gameplan-1312c-export.json", 'r') as f:
        firebase = json.load(f)
    users = firebase["users"]

#import DB from firebase
    #from firebase import firebase
    #firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)
    #users = firebase.get('users', None)


    #print users.get('CEphnEaDLuWnjgQUsuSj40AutoC3')
    usersDict = {}
    for userId,userData in users.items():
		#usersDict[userId] = {}
		#usersDict[userId]['age'] = userData['age']
        print(userId)
        #print (userData)
		#print users[userId]
        attendingPosts = userData["attending"]
        for year,yearData in attendingPosts.items():
            print ("*year*")
            print (year)

            for month,monthData in yearData.items():
                print ("*month*")
                print(month)
                #print(yearData[11])
                for dayData in monthData:
                    print ("*day*")
                    print (dayData)
                    if dayData is not None:
                        for aa in dayData:
                            print ("*aa*")
                            print (aa)












if __name__ == '__main__':
	main(sys.argv)

