Fuzzy C-Means algorithm for image clustering
==================================================


This program was written for the university course of discrete mathematics during june of 2012.


## Introduction

When we look at the image all we see is just a bunch of pixels. Each pixel consists of some data, for example the values of r, g, b channels. There are infinite ways in which one could group them. As the problem is very common there are many tested algorithms for this task. [Fuzzy C-Means] is one of them.


## Example results

![alt text][img1]
*Image with limited color palete*

![alt text][img2]
*Detailed image*

## Usage

All configuration can be changed be modifying the [Main] class.

For single image:

1. Set the **_directory_** to **false**
1. Set the **_dir_** to point to directory of the image
1. Set the **_fileName_** to name of the image ( including file extension)
1. Run the program
1. After the program finishes the window will pop up with the results
1. Result image will be saved to **_out_** directory

For directory of images:

1. Set the **_directory_** to **true**
1. Set the **_dir_** to point to the selected directory
1. Run the program
1. Result images will be saved to **_out_** directory

####Other parameters
* **_c_** number of clusters
* **_m_** parameter of *fuzzines*
* **_e_** minimum accuracy of the result
* **_storeResult_** if result should be saved to disk
* **_colors_** array of color used to colorize the result ( should be bigger then **_c_** !)
* **_maxIteration_** number of iterations

#### How do I stop the program ?
* wait till the result is accurate enough ( parameter **_e_**)
* wait for all iterations to complete ( parameter **_maxIteration_**)
* write **stop** in the console


## Other

* During class we were presented with example calculations that I have used for testing. For some reason I can't find the tests :(
* I've been using mercurial at the time so the original history is gone
* Enclosed report is terribly written. No matter how much better I got at writing software I've got infinitely better at writing documentation
* As the program was to be further modified by other students all comments were required to be in Polish


## Thanks

I would like to thank Michał Laskowski for allowing me to use his laptop during the original presentation.

[Fuzzy C-Means]:http://en.wikipedia.org/wiki/Fuzzy_clustering#Fuzzy_c-means_clustering
[Main]: src/main/Main.java
[img1]: https://raw.github.com/Scthe/fcm-images/master/cold_reflection_by_lildream_res.jpg "Image with limited color palete"
[img2]: https://raw.github.com/Scthe/fcm-images/master/uncle_sam_res.jpg "Detailed image"
