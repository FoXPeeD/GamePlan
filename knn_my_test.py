import numpy as np
from sklearn.neighbors import NearestNeighbors
# from firebase import firebase
# firebase = firebase.FirebaseApplication('https://gameplan-1312c.firebaseio.com/', None)
# users = firebase.get('users', None)
# print users.get(CEphnEaDLuWnjgQUsuSj40AutoC3)


samples = [[1, 0, 0], [0, 0, 2], [0, 0, 6], [0, 0, 1.3]]



neigh = NearestNeighbors(algorithm='auto', metric='minkowski')
neigh.fit(samples)


print ('result:')
print (neigh.kneighbors([[0, 0, 1.3]], 2, return_distance=False))


print ('result2:')
nbrs = neigh.radius_neighbors([[0, 0, 1.3]], 0.4, return_distance=False)
np.asarray(nbrs[0][0])