from firebase import firebase
firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)
users = firebase.get('users', None)
#print users.get('CEphnEaDLuWnjgQUsuSj40AutoC3')

usersDict = {}
for userId in users.keys():
	#usersDict[userId] = {}
	#usersDict[userId]['age'] = userData['age']
	print userId
	print users[userId]