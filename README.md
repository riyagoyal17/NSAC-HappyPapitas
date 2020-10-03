# NSAC-HappyPapitas

# Introduction

The goal of the project is to create an API/mobile app for detecting the air quality of a place at a given time, and thus comment on the air quality standards and pollution in that particular place.

.
# Data Used

The labelled dataset provided on the spaceApps official github repository is used.
Along with the original dataset, a weather API is also ***to be*** used to collect the real time data.
Also, data for relation of PM 2.5 levels with air quality index is used.


# Data Visualization
To be filled(in the jupyter file)

# Preprocessing

The dataset recieved has various scales for different fields.
The PM 2.5 data was converted to categorical data with four values from 0(low PM 2.5 levels) to 3(very high concentration of PM 2.5 particles).
Also, a variable for windspeed was included using UGRD and VGRD values. 
All the attributes were then scaled and normalized.
The dataset was then divided into training and testing data.


# Model Framework

Tensorflow with keras was used as the framework.
We decided to use the sequential model as each data point was independent from others.
A 4-layered NN was used as the  architecture for the model.


# Training the model

The model was trained for 15 epochs with a batch size of 16 and the loss and accuracy at the end of training period were 0.0783 and 0.9159 respectively.


# Testing and results

The model performed adequately on the test set with a loss of 0.7975 and an accuracy of 0.9086.

# Further tasks

To create a model that could perform regression task of calculating the values for PM 2.5 values rather than creating a classification model.
To create a mobile app that could provide real time AQI(air quality index : *a direct measure of PM 2.5 levels*) .
