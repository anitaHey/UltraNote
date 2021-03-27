<div align="center">
<h1>UltraNote</h3>
<img width="1000" src="/.meta/login.JPG">
</div>

## Overview
UltraNote is a desktop application which is similar to Microsoft OneNote. This application provides multiple general features like word edit, picture showing, painting, etc. It also adds some extra features which other notebook apps don't have, like simple programming.

This project wants to provide a new using experience that differs from other apps, it can combine most of the features we use for noting.

<br>

* Three-tier [REST architecture](https://en.wikipedia.org/wiki/Representational_state_transfer)
  - **Client** - Java FX 
  - **RESTful API** - Java 8
  - **Backend** - Python 3.5 + Django 1.8.18 

## Basic Node
Each insertable object in UltraNote is a node which can move freely, and it can also resize and rotate.

## Textbox
<img width="800" src="/.meta/text.gif">
Textbox including the change of text color, font family, font size, underline, italic, and bold. Because JavaFX's textarea can't change the font style, so I try to make a new textarea.

<br><img width="600" src="/.meta/text_structure.png">

Each line is Class TextLine which extends VBox. and each charcter is Class TextObj which extends Lable. So I can change the font style by changing lable's style in TextLine. And underline is another line in TextLine that implement by filled pane.

## Picture
<img width="800" src="/.meta/picture.gif">
Picture can add border and change the style of border. Besides, it can crop proportionally even if picture is resized.

<br><img width="600" src="/.meta/pic_structure.png">

For the crop feature, I create a CropImage class which will record picture's FileInputStream, so that it can calculate the zoom ratio of the picture.
