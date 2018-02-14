# Eyelix
Ophthalmology kit for android devices


## Backend Image Recognition

1) Import image from the API made
2) Clone image into Grayscale
3) Median Blur on cloneimage finding median of intensities at an interval of every 3x3 pixel array.
4) Hough function circular transformation was applied with appropriate threshold parameters to extract the iris from the image.
5) A circular numpy array was created containing the iris region.
6) The coordinates of the centre of the iris and pupil and the radius of iris was obtained by the numpy array.
7) The original image was converted into HSV color scheme. The intensity elements were split apart, ‘v’ was incremented by 250 and the three color aspects were merged back together and image was reverted to BGR colr scheme, with higher pixel intensity this time.
8) Original Image was cropped according to the coordinates of the numpy array  previously obtained using the clone image.
9) The region outside the iris was removed by solving a simple circular inequality and omitting the region outside the circle.
10) Another Hough Function Transformation was performed on the iris after the the intensity was increased to extract the pupil.
11) A numpy circular array was created to obtain the coordinates and radius of the pupil.

### Glaucoma Detection:

<source=https://www.researchgate.net/publication/306392214_PupilIris_Ratio_Determination_for_IOP >

In the eye of a Glaucoma patient, the fluid pressure is high which makes it seem dilated.
According to a research paper by Mohammad Aloudat and Miad Faezpour in IEEE Engineering in Medicine and Biology Society conference, the ratio of the diameter of Pupil to the diameter of Iris should be less than 0.69 for a healthy eye. If the ratio crosses the threshold value, the test is positive for Glaucoma.

1) Find the ratio  of the radius of Iris and Pupil using the coordinates obtained from the numpy array.
2) Compare the value to the threshold ratio for Glaucoma.
3) Return a discrete 0 or 1 value to the API indicating whether the test was negative or positive.

### Cataract Detection:

 In a research article by Shashwat Pathak and Basant Kumar from the Department of ECE,  MNNIT- Allahabad, a procedure for determining the presence of cataract on the lens of a person, by image processing and analysis was mentioned. The algorithm is proposed for cataract screening based on texture features: uniformity, intensity and standard deviation. Each parameter had a certain threshold value 

Preprocessing includes conservative smoothing followed by image denoising. The isotropic
Gaussian filter is widely used as a low-pass filter for image de-noising. A two dimensional
(2D) Gaussian function is simply the product of two 1D Gaussian functions. We used Median functions instead.

Feature extraction is done after preprocessing to extract all the information for cataract detection
and grading from the circular pupil region. The detection algorithm was based on finding the
accurate thresholds of texture feature parameters such as image intensity (I), uniformity (U) standard deviation (s), to distinguish between healthy and abnormal eyes. In cataract eyes, the whitish color originates from the lens region so it can be easily concluded that cataract eyes have higher intensities than normal eyes.



1) The rows and columns of the original image were iterated
2) Radius of Pupil was defined
3) The BGR elements of each element within the radius of the pupil were individually considered, and their average intensity and their mean deviation was found.
4) The extracted parameters were compared to the values mentioned in the research article to determine the presence of Cataract in the eye of the user.


# Deployment on an Android Application



1) Android Activity started
2) Images are chosen either from gallery or from camera
3) Images are converted into base64 strings then sent to the Python backend through an http request on an AWS server.
4) The Python API converts the base64 string into image.
5) The API fetches the image to the Program written using OpenCV Libraries, extracts the returned variables and sends them back to the Android Device on which the results are shown.
