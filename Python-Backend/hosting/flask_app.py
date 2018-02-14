"""
request url example - https://localhost:8000/api/name="test"&id=2
above get request prints data as dict {"name": "manan", "id": 2}
"""
from flask import Flask
from flask_restful import Resource, Api, reqparse
import cv2
import numpy as np
from math import hypot,sqrt
import base64
from PIL import Image
import io


def parse_payload(string):
	query = {}
	for s in string.split("&"):
		
		s = s.split("=")
		query.update({s[0].strip(): s[1].strip()})
	return query


app = Flask(__name__)
api = Api(app)


class ApiTest(Resource):
	def get(self, query):
		data = parse_payload(query)
		
		# do processing on data
		finalStr=SlashError(data["name"])
		imgdata = base64.b64decode(finalStr)
		filename = 'some_image.jpg'  # I assume you have a way of picking unique filenames
		with open(filename, 'wb') as f:
		    f.write(imgdata)
		img=cv2.imread(filename,1)

		if data["id"]=="1":
			return glaucoma(img)
		else:
			return catarct(img)


def glaucoma(imgR):
	img = cv2.cvtColor( imgR, cv2.COLOR_BGR2GRAY)
	img = cv2.medianBlur(img,3)
	circles = cv2.HoughCircles(img,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=30,maxRadius=0)
	circles = np.uint16(np.around(circles))

	x, y, r = circles[0,:][0]   # Coordinates and Radius of the Iris which can be used for detecting Glaucoma
	rows, cols = img.shape

	print x,y,r




	# Brightness ++++

	hsv = cv2.cvtColor( imgR, cv2.COLOR_BGR2HSV)
	h,s,v = cv2.split(hsv)
	v += 250
	final_hsv = cv2.merge ((h,s,v))

	imgR = cv2.cvtColor(final_hsv, cv2.COLOR_HSV2BGR)
	img = cv2.cvtColor(imgR, cv2.COLOR_BGR2GRAY)

	# Crop And Shift Origin : FAILED


	img = img[ y-r : y+r , x-r : x+r]
	y = r
	x = r

	rows, cols = img.shape


	# Removing Whitespace

	for i in range(cols):
	    for j in range(rows):
	        if hypot(i-x, j-y) > r:
	            img[j,i] = 0

	

	img=img[30:2*r-30,30:2*r-30]
	y=r-30
	x=r-30





	#img2 = cv2.threshold(img2 , 15 , 125, cv2.THRESH_BINARY)
	circles1 = cv2.HoughCircles(img ,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=0,maxRadius=0)
	circles1 = np.uint16(np.around(circles1))
	#circles2 = cv2.HoughCircles(img2 ,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=1,maxRadius=0)
	#circles2 = np.uint16(np.around(circles2))

	x2, y2, r2 = circles1[0,:][0]


	ratio = r2*1.0/r
	return str(ratio)








def catarct(imgN):
	img = cv2.cvtColor( imgN, cv2.COLOR_BGR2GRAY)
	img = cv2.medianBlur(img,3)
	circles = cv2.HoughCircles(img,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=30,maxRadius=0)
	circles = np.uint16(np.around(circles))

	x, y, r = circles[0,:][0]   # Coordinates and Radius of the Iris which can be used for detecting Glaucoma
	rows, cols = img.shape

	print x,y,r

	xr=x
	yr=y




	# Brightness ++++

	hsv = cv2.cvtColor( imgN, cv2.COLOR_BGR2HSV)
	h,s,v = cv2.split(hsv)
	v += 250
	final_hsv = cv2.merge ((h,s,v))

	imgR = cv2.cvtColor(final_hsv, cv2.COLOR_HSV2BGR)
	img = cv2.cvtColor(imgR, cv2.COLOR_BGR2GRAY)

	# Crop And Shift Origin : FAILED


	img = img[ y-r : y+r , x-r : x+r]
	y = r
	x = r

	rows, cols = img.shape


	# Removing Whitespace

	for i in range(cols):
	    for j in range(rows):
	        if hypot(i-x, j-y) > r:
	            img[j,i] = 0

	

	img=img[30:2*r-30,30:2*r-30]
	y=r-30
	x=r-30





	#img2 = cv2.threshold(img2 , 15 , 125, cv2.THRESH_BINARY)
	circles1 = cv2.HoughCircles(img ,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=0,maxRadius=0)
	circles1 = np.uint16(np.around(circles1))
	#circles2 = cv2.HoughCircles(img2 ,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=1,maxRadius=0)
	#circles2 = np.uint16(np.around(circles2))

	x2, y2, r2 = circles1[0,:][0]


	rows2, cols2,__ = imgR.shape

	xn=x2+30+xr-r
	yn=y2+30+yr-r

	# print x2,y2,r2
	npixels=0
	intensitySum=0
	bgrList=[]
	for i in range(cols2):
	    for j in range(rows2):
	        if hypot(i-xn, j-yn) < r2:
	        	npixels+=1
	        	b=imgR[j,i][0]
	        	g=imgR[j,i][1]
	        	r=imgR[j,i][2]
	            #calculate intensity
	        	intensitySum+=b
	        	intensitySum+=g
	        	intensitySum+=r
	            # #calculate unformity

	            #caculate deviation
	        	bgrList.append(b)
	        	bgrList.append(g)
	        	bgrList.append(r)

	intensity = (intensitySum*1.0)/(3.0*npixels)
	devSum=0
	for f in bgrList:
		devSum+=(f-intensity)*(f-intensity)
	devSum=sqrt(devSum)
	devSum=devSum*1.0/(81*sqrt(((2*r2)*(2*r2))-1))

	return str(devSum)+"&"+str(intensity)


def SlashError(string):
    string = string.replace('@', '/')
    string = string.replace('^', '=')
    return string

# def stringToBGR(base64_string):
#     imgdata = base64.b64decode(str(base64_string))
#     image = Image.open(io.BytesIO(imgdata))
#     image_data = np.asarray(image)
#     return image_data


api.add_resource(ApiTest, "/api/<string:query>")
if __name__ == '__main__':
	parser = reqparse.RequestParser()
	app.run(debug=True, host="0.0.0.0", port=8000)
