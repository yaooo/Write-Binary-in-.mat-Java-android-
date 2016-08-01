main.java is the testing file running in eclipse.

writeFileInBinary.java is a constructor that helps write the Matlab header and input numbers into a file.
Currently, there are some flaws:

1. limited by the matlab format, we have to know how many data points are stored. However, if we pause recording and recall this constructor, we will not able to track how maany data points were recorded previously.
2. Inside of the constructor, setting path for the external storage of Android is commented out for the testing purpose. Feel free to change the constructor declaration a bit in order to make it work.
3. Currently, we only support write double/short/byte into binary, as they are used the most often.
