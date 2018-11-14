import sys
def main(argv):

	from firebase import firebase
	firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)
	users = firebase.get('users', None)
	#print users.get('CEphnEaDLuWnjgQUsuSj40AutoC3')
	usersDict = {}
	for userId,userData in users.items():
		#usersDict[userId] = {}
		#usersDict[userId]['age'] = userData['age']
		print userId
		#print users[userId]











if __name__ == '__main__':
	main(sys.argv)

