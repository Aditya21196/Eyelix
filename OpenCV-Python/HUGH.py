import cv2
import numpy as np
from matplotlib import pyplot as plt
from math import hypot,sqrt

imgR = cv2.imread('/home/overkill/Desktop/Practice/OpenCV/nintchdbpict000289876822.jpg',1)
img = cv2.cvtColor( imgR, cv2.COLOR_BGR2GRAY)
# Median Blur at 3

img = cv2.medianBlur(img,3)




# Hough Transformation and numpy array circular function tracing

circles = cv2.HoughCircles(img,cv2.HOUGH_GRADIENT,1,120,param1=50,param2=50,minRadius=30,maxRadius=0)
circles = np.uint16(np.around(circles))

x, y, r = circles[0,:][0]   # Coordinates and Rad
rows, cols = img.shape

xr=x
yr=y

print x,y,r




# Brightness++

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

cv2.imwrite("iris.jpg",img)

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

print devSum,intensity

#Glaucoma
#ratio = r2/r1
#if ratio > 0.69 :
#    IsGlaucoma = True
#else:
#    IsGlaucoma = False



#cv2.imwrite("iris.jpg",img)


#cv2.imwrite("pupil.jpg",img)
#plt.imshow(img2, cmap = 'gray', interpolation = 'bicubic')
plt.imshow(img, cmap = 'gray', interpolation = 'bicubic')
plt.xticks([]), plt.yticks([])
plt.show()
