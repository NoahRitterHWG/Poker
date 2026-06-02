Poker (Texas Hold'em) - Project Documentation
Overview

This project is a Java implementation of Texas Hold'em Poker.

The current version focuses on:

Card and deck management
Betting rounds
Hand evaluation
Winner determination
Blind rotation
Tie-break calculations

The game is currently played through Swing dialogs (JOptionPane), but the long-term goal is to separate the game logic from the user interface so that a web frontend can be built on top of the existing backend.

Game Rules
Objective

The goal is to win chips by:

Having the best 5-card poker hand at showdown.
Making all other players fold before showdown.
Texas Hold'em Structure

Each player receives:

2 private cards ("Hole Cards")

The table receives:

5 community cards

Players create the strongest possible 5-card hand using any combination of:

Their 2 hole cards
The 5 community cards
Hand Rankings

Highest to lowest:

Straight Flush
Four of a Kind
Full House
Flush
Straight
Three of a Kind
Two Pair
One Pair
High Card
Blinds

Each round:

One player posts the Small Blind (100)
One player posts the Big Blind (200)

Current implementation:

Small Blind = 100
Big Blind   = 200

The blind positions rotate every round.

Game Flow
1. Start Game

The game asks:

Number of players (2-10)
Names of all players

Players are created and stored in:

ArrayList<Player> players
2. Start Round

A new round:

Rotates player order
Creates and shuffles a deck
Deals 2 cards to every player
Selects 5 community cards
Collects blinds
3. Pre-Flop Betting Round

Players act in order:

Possible actions:

Fold
Call / Check
Raise
4. Flop

3 community cards are revealed.

Another betting round begins.

5. Turn

1 additional community card is revealed.

Another betting round begins.

6. River

1 final community card is revealed.

Final betting round begins.

7. Showdown

Remaining players:

Evaluate their best possible hand.
Compare hand strengths.
Apply tie-break rules if needed.
Determine winner.
Award pot.
8. Continue Game

Players decide whether to continue.

If yes:

New round starts.

If no:

Player is removed.
Project Structure
src/
│
├── App.java
├── Game.java
├── Player.java
├── Deck.java
├── Card.java
├── Middle.java
│
├── CardType.java
├── CardValues.java
├── PokerHand.java
├── PlayerAction.java
├── PlayerMove.java
│
└── CardPoints.java
Class Documentation
App

Entry point of the application.

Responsibilities
Creates Game object
Starts the game
Methods
main(String[] args)

Starts the application.

Game

Main controller of the poker game.

Responsibilities
Manage rounds
Manage betting
Manage player order
Manage blinds
Determine winner
Control overall game flow
Fields
players
playersInRound
stoppedPlaying
middle
currentTargetBet
smallBlindIndex
bigBlindIndex
Methods
getPlayers()

Returns all active players.

startGame()

Initial setup:

Gets player count
Gets player names
Creates players
Starts first round
startRound()

Starts a complete poker round.

Tasks:

Rotate blinds
Create deck
Deal cards
Collect blinds
Run betting rounds
Trigger showdown
showdown()

Displays all hands and determines the winner.

betRound()

Runs a betting phase until all players have acted correctly.

handleAction()

Processes:

Fold
Call
Raise

Updates:

Pot
Player balance
Current bet
askPlayerMove()

Prompts the current player for an action.

Returns:

PlayerMove
determinedWinner(Middle middle)

Evaluates all remaining players and returns the winner.

askForContinuation()

Asks whether players want to continue playing.

resetplayers()

Resets round-specific player values.

newLastRaiser()

Updates which player is currently the last raiser.

askForValidInt()

Utility function for validated integer input.

Player

Represents a poker player.

Responsibilities
Store player data
Store cards
Evaluate poker hands
Calculate tie-break values
Fields
name
id
playerBalance
handCards
bestPossibleHand
bestHand
roundBet
Methods
takeCards(Deck deck)

Deals 2 cards to the player.

evaluate(ArrayList<Card> communityCards)

Evaluates all 21 possible 5-card combinations.

Returns:

PokerHand

The strongest possible hand.

winsTiebreak(...)

Compares two tie-break arrays.

Determines which hand wins when hand ranks are equal.

getTiebreakValues()

Returns the currently stored tie-break values.

determineHandType(...)

Detects:

Pair
Two Pair
Straight
Flush
Full House
etc.
getTiebreakValuesForHand(...)

Builds tie-break values for a specific hand type.

isFlush(...)

Checks for flush.

isStraight(...)

Checks for straight.

isFullHouse(...)

Checks for full house.

isTwoPair(...)

Checks for two pair.

hasNOfAKind(...)

Generic N-of-a-kind detection.

Used for:

Pair
Trips
Quads
getRankOfN(...)

Returns rank of matching cards.

hasRankCount(...)

Helper method for hand detection.

resetplayer()

Resets round-specific values.

hasStoppedPlaying()

Returns whether player has left the game.

Deck

Represents a standard 52-card deck.

Responsibilities
Generate cards
Shuffle cards
Provide cards for dealing
Methods
getCards()

Returns card list.

shuffledeck()

Randomly shuffles the deck.

Card

Represents a single playing card.

Fields
CardType
CardValues
Methods
getValue()

Returns numerical card value.

toString()

Returns formatted card string.

Example:

A-S
10-H
K-D
Middle

Represents the board/community cards.

Responsibilities
Store community cards
Reveal cards
Track pot
Methods
revealCard(int amount)

Reveals community cards.

takeCards(Deck deck)

Draws the 5 community cards.

getMiddleCards()

Returns community cards.

PlayerMove

Stores a player's chosen move.

Fields
PlayerAction action
int raiseAmount
Methods
getAction()

Returns selected action.

getRaiseAmount()

Returns raise amount.

Enums
PokerHand

Represents hand rankings.

HIGHCARD
ONE_PAIR
TWO_PAIR
THREE_OF_A_KIND
STRAIGHT
FLUSH
FULL_HOUSE
FOUR_OF_A_KIND
STRAIGHT_FLUSH
PlayerAction
FOLD
CALL
RAISE
CardType
CLUBS
HEARTS
DIAMONDS
SPADES
CardValues
A
TWO
THREE
FOUR
FIVE
SIX
SEVEN
EIGHT
NINE
TEN
J
Q
K
Known Bugs

Current README notes:

PossibleHand is size 3 instead of 5
getTiebreakValue returns null because One Pair and Two Pair are not working correctly

These issues should be investigated before production use.

Unfinished / Improvement Areas
Frontend Separation

Currently the game logic directly uses:

JOptionPane

for:

Input
Output
Error messages

This makes frontend integration difficult.

Recommended:

Move all UI code out of Game.java
Expose game state through getters
Let UI send actions into the game engine
Hand Evaluation Testing

The following require extensive testing:

One Pair
Two Pair
Tie-break logic
Full House edge cases
Straight edge cases (Ace high / Ace low)
Missing Features

Potential future features:

Side pots
All-in support
Split pots
Multiple winners
AI opponents
Statistics
Save/load games
Web frontend
Network multiplayer