# blackjack-sp
#### Video Demo: <URL Pending>
#### Description:
This is a single player blackjack game developed entirely using Java. While there are other, better documented libraries/frameworks out there for game development, my primary motivation with this project was to learn how to code in Java, as my prior experiences were mostly in C with some Python as well. My other main goal was to gain a better understanding of OOP, a goal which this project definitely accomplished. The front end UI is implemented using the Java Swing framework, which allows for the creation of window-based applications, and is built on top of the older AWT framework.
  
  Before writing any code, I took a day to write out and diagram my plans for the project on paper. While the final product is far from my initial model, the value of having some sort of idea to work off of was immense, and gave me a list of things to create and implement.
  
  This project was undertaken in two major sections. I started out by writing the backend. The game was initially implemented as a text-based command line game as I tried to get comfortable with the basics of Java and OOP. After finishing the command line game I had to repurpose/reform some of the backend functionality to better align with my goals for a GUI. This exercise helped me develop a better understanding of how to write a backend that can be used to supply visual input for a variety of different game mediums (in this case text and graphical).
  
  I began by setting up ENUMs to hold the suits and card values. ENUMs are unique in Java, in that they serve to do more than simply hold a set of static data that you want to be able to reference repeatedly. They actually allow you to invoke a constructor for each ENUM value, creating a sort of class for each one, with the option to add additional subfields of your choosing. This was vital in my design, as my CARDVALS enum allowed for the storing of both a point value and a name value within each ENUM (ie KING(title: "King",val: 10). 
  
  Afterwards, I created the objects I needed for the game, starting with the smallest (cards) and working up to the game itself. While creating my classes for cards I did some reading on best practices and was introduced to getters and setters, learning the importance of keeping internal fields private where possible, so as to avoid accidental changes at different points in the program.
  #### Backend Classes:
  - CARDVALS/SUITS: The two enums that are used to create each unique card in a 52 card deck. Each ENUM value is sort of its own class, with subfields laid out by a constructor set up within the ENUM. Functions are specified to retrieve the ENUM subvalues here.
  - Card: Creates each card used in the playing deck. Each card is given a value and a suit from the ENUMs when constructed, as well as an isHidden boolean value which is used to determine whether or not it can be seen by the user (one of the dealer's cards stays hidden in blackjack until their turn). Cards does not include methods other than getters and setters.
  - Player: Houses all players (including the dealer). Players are given a name as well as a boolean to determine whether or not they are the dealer. This isDealer boolean is vital for some gameplay functionality, as the dealer and player have different mechanics in the game. Players also needed the ability to hold a hand of cards, which is created using an ArrayList of the Card class. Cards can be added to the hand one at a time with the addCard method, which works in tandem with the dealCard method to be described in the CardPool class. Player also has a handValue method, which returns the optimal hand value for a player given the cards in their hand.
  
  

