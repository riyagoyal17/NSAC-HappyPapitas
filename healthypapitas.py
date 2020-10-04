import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import tensorflow as tf
from keras import layers
import keras
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler

df = pd.read_csv('PM2.5_dataset.csv')
df = df.drop(columns = ('index'))

df['PM_buckets'] = pd.cut(df['air_data_value'], bins = [0,15.5,40.5,65.5,150] , labels = [0,1,2,3])
df['TMP'] = df['TMP'] - 273

df['wind_speed'] = pd.Series(tf.sqrt(df['UGRD']*df['UGRD'] + df['VGRD']*df['VGRD']))
df.corr()

df.groupby('station_id')['air_data_value'].sum()

df['air_data_value'].describe()

df = df.drop(columns = 'station_id')
df = df.drop(columns = 'stime')
df = df.drop(columns = 'UGRD')
df = df.drop(columns = 'VGRD')
df = df.drop(columns = 'air_data_value')

X = df.drop(columns = ('PM_buckets'))
y = df['PM_buckets']

X_train,X_test,y_train,y_test = train_test_split(X,y,test_size = 0.2,random_state = 1)

model = keras.Sequential([
    keras.Input((5)),
    layers.Dense(128,activation = 'relu'),
    layers.Dense(32,activation = 'relu'),
    layers.Dense(1)
])

model.summary()

model.compile(optimizer = 'adam',  loss = 'mean_squared_error',metrics = "accuracy")

model.evaluate(X_test,y_test)

y_pred = model.predict(X_test)
