# Guesser Agent for the ESSENCE Taboo Challenge

Taboo is a word-guessing game in which one player has to describe a target term to another player by giving hints that are neither the target term nor other terms specified in a predetermined list of taboo words. For example, a player might have to describe water without using sea, blue or beverage.

The Location Taboo Challenge (LTC), which has been proposed by the [ESSENCE Marie Curie Initial Training Network](https://www.essence-network.com/), is a version of Taboo that only contains cities as target terms and is intended to be played by artificial guesser agents. In the LTC, the hints, which are words associated to the target city, are sequentially provided to the guesser agent, and the goal is to guess the target location as soon as possible. A complete specification of the challenge can be found [here](https://www.essence-network.com/challenge/).

This repository provides the code of a Guesser agent presented in a [workshop paper](https://github.com/keminus/GuesserAgent#Publication). This readme will guide you in the installation of the agent.

## Presentation of the Taboo Game

An LTC game is played by two agents, the describer and the guesser. The game starts with the describer, providing a hint about a particular city anywhere in the world. Based on this hint, the guesser tries to guess the city that is being described. There are two possi- ble outcomes after a guess has been made. For the outcome where the guess is correct, the game is considered to be successful. However, for the outcome where the guess is incorrect, the describer provides another hint and the game continues until the describer has consumed all the hints. The LT Challenge consists of implementing a guesser agent that can guess the correct city using the fewest number of guesses possible and before the describer runs out of hints. In the case where the describer runs out of hints and the correct guess has not yet been made, the game is considered to have failed.
For the LTC, the describer agent is provided by the authors of the challenge and the hints are crowd-sourced from real games played by human players. Therefore, the length of a game - i.e. the number of hints - is not fixed, but determined by the individual players. Also, it should be noted that the real-world dataset, which is pro- vided by ESSENCE Network, consists of only successfully finished games. After each guess, the describer provides not only a new hint, but also the city that the human player (wrongfully) guessed. This information may be useful, or even necessary, in order to interpret
 
the next hints, as these might be relative to the guesser’s previous guesses (e.g. ’north’ or ’close’). Hints are usually single words, but can occasionally be multi-word expressions. According to the rules of the LTC, the hints do not include proper names.

## Prerequisites

This project has been developed using [Eclipse](https://eclipse.org/). The repository is a standard eclipse project folder. Since the code is written in [Java](https://www.java.com/fr/), any other IDE and it can be executed from a terminal in any operating system that has Java installed. However, the code has only been tested in the Eclipse environment so we recommend you to use it.

### Java

Download the [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) version compatible with your OS and run the installation.

### Eclipse

You can download Eclipse [here](https://eclipse.org/downloads/) for a large panel of operating systems. A good tutorial for the installation is provided by the Eclipse [Wiki](https://wiki.eclipse.org/Eclipse/Installation). You should download the last Eclipse IDE for Java Developers version of the IDE.

## Installation

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Import the repository files in Eclipse

Follow the next steps to import our project in Eclipse:

```
>Step 1: Copy your local repository folder in your current workspace. If this is the first time that you launch Eclipse, it will ask you to create a workspace. Note its direction and copy the local repository there.

>Step 2: In the window that pops up, select Java Project and clic next.

>Step 3: In the Project name field, enter 'GuesserAgent' (or the name of the local folder of this repository if you changed it).

>Step 4: Clic on Finish.
```
Or see how to import an Eclipse project in the Eclipse [Wiki](https://wiki.eclipse.org/Eclipse/Installation).

### Place the complementary files in the Eclipse project

Two files need to be added to the project in order to run the guesser agent. They are not provided in the repository and should be added to the root of the GuesserAgent folder (or the name of the local folder of this repository if you changed it).

```
cities.tsv
Taboo_cleaned_onlyPerfects.csv
```

If you want to modify these names, see the [Javadoc](https://keminus.github.io/GuesserAgent/) of the classes from the Tester package.

cities.tsv is a spreadsheet file with columns separated by tabulations. This file contains informations about the cities that the guesser knows about. Each line correspond to a different city. The file has six columns, respectively:

```
1- a unique identifier for the city
2- the name of the city name
3- the name of the city's country
4- the latitude of the city
5- the longitude of the city
6- a list taboo words for the city, each separated by a semicolon
```

example:

```
1	Hiroshima	Japan	3.438.520.300	13.245.529.300	atomic bomb; castle; park; humid; gardens; rebuilt; coastline; war; pancake; shrine; cherry
```

Taboo_cleaned_onlyPerfects.csv is a spreadsheet file with columns separated by commas. This file contains a set of recorded Taboo games, in order to train the guesser. Each line figures a different turn from a Taboo game, with the message exchanged during this turn. The file has ten columns, respectively:

```
1- a unique identifier for the game
2- the city's unique identifier (similar to the one in cities.tsv)
3- the city's name
4- blank column
5- "guesser" or "describer", depending on which is playing during the turn
6- a string with the message sent to the other player
7- a list taboo words for the city, each separated by a semicolon
8- a unique identifier for this message
9- the name of the user account that sent the message
10- the date of the message
```

The file should start with the header:

```
game_id , city_id , city_name , FLAG , role , message , tabooWords , message_id , user_name , mes_time
```

After the header, each line is built on the following model:

```
9475366 , 25 , Barcelona , , describer , No. sea , flamenco; architecture; art; gardens; football; waterfront; dance; cathedral; culture; independent; rich; autonomous , 555 , nschneid , 18.05.2016 15:52:45
```

## Running the tests

### With hints from a real time Human describer

In order to play directly against the guesser agent, go in the Tester package and run the class against_human. You should see a message like this in the terminal (probably with another city than Osaka):

```
You have to make the computer guess Osaka
You cannot use the words (or derivated) :  |  culture |  mountains |  bustling |  udon |  ramen |  baseball |  vibrant | tofu
```
You can write a hint in the terminal. The guesser will try to guess the city:

```
the computer thinks that you mean Hawaii
You have to make the computer guess Osaka
```

and the game will repeat until the guesser finds the right city.

### With hints from the recorded games in Taboo_cleaned_onlyPerfects.csv

In order to play with hints from recorded games, go in the Tester package and run either the class extensive_game or until_success_game. They share the same code excepet the stop condition of the game (no hint left for the first, success for the second).

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [JWI](https://projects.csail.mit.edu/jwi/) - Used to create java objects from Wordnet
* [Wordnet](https://wordnet.princeton.edu/) - Used as a database for semantic distances

## Publication

You can find the publication about our guesser agent in the proceedings of ECAI 2016 DIVERSITY Workshop [here](http://www.ecai2016.org/content/uploads/2016/08/W13-diversity-2016.pdf). 

## Authors

* [Kemo Adrian](https://www.iiia.csic.es/en/staff/kemo-adrian)
* [Aysenur Bilgin](http://www.uva.nl/over-de-uva/organisatie/medewerkers/content/b/i/a.bilgin/a.bilgin.html)
* [Paul Van Eecke](https://scholar.google.com/citations?user=WWZim-8AAAAJ&hl=en)

## License

Maven is under [Apache License, Version 2.0 (ALv2)](https://www.apache.org/licenses/).
JWI is under [Creative Commons Attribution Version 4.0 International Public License](https://projects.csail.mit.edu/jwi/license.html).
Wordnet is under [WordNet 3.0 license](https://wordnet.princeton.edu/wordnet/license/).

## Acknowledgments

The authors are supported by the project ESSENCE: Evolution of Shared Semantics in Computational Environments (ITN 607062)
