# JPEG-like compressing algorithm 

JPEG based image compression written in Java. Actually the same parts as in JPEG and now ready are:

1. Discrete cosine transformation
2. Quantization

3. Huffman coding is done with constructing a trie, not with statistical way as JPEG does. 

Source code is in jpeg/src folder.

#Usage

1. Compress .rgb -image
```
java -jar "/home/username/JPEG/jpeg/dist/jpeg.jar" image.rgb image.gpeg height width

```
2. Show .gpeg image 

```
java -jar "/home/username/JPEG/jpeg/dist/jpeg.jar" image.gpeg height width
```
