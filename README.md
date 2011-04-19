# libgdx sbt project

A [g8](http://github.com/n8han/giter8) template to get a [libgdx](http://code.google.com/p/libgdx/) sbt project up and running in a matter of seconds.

## How to use

To use this template, you will need to install g8 first.
Read g8's [readme](http://github.com/n8han/giter8#readme) for more information.

Then, in your favorite shell, type the command:

    g8 ajhager/libgdx-sbt-project

This will prompt you for a few parameters. Type in the options you would like to change, or just press enter to accept the default. For example, if I wanted to create the next greatest Asteroids clone:

    package [my.game.pkg]: com.ajhager.hageroids
    name [Game]: Hageroids
    android_api_level [11]:
    scala_version [2.8.1]:
    sbt_version [0.7.5]:

You can see that I created a new package, com.ajhager.hageroids, for my new game Hageroids. I just pressed enter to accep the lastest android api level, scala version, and sbt version. Next I just need to download all the needed libraries:

    cd Hageroids
    sbt update

This will download your scala, sbt, and the libgdx nightly build. Soon you will be able to choose any version of libgdx you'd like. For now, every time you update in the main directory, the bleeding edge libraries will be fetched and installed for your project.

## Credit
This g8 template is based on the project generated by [n8han](http://github/n8han)'s [android-app](https://github.com/n8han/android-app.g8).

## Todo

     * Finish readme with more information and a tutorial
     * Add sbt options for choosing the desktop backend and libgdx version
     * Add one step desktop packaging and deployment
     * Add webstart project and deployment
     * Add android package signing
