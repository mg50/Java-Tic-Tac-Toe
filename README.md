
Project contains three implementations of Tic-Tac-Toe: a Java console version, a JRuby web version and a Limelight version.

Make sure you have Java installed, as well as JRuby 1.6.4. Type 'bundle install' in the root directory to install gem
dependencies from the gemfile. Also make sure the latest version of Ant is installed. Run "ant jar" before any of the
following.

============
JAVA VERSION
============

Type "ant test" to run tests and "ant jar" to build the ttt.jar and put it in the javattt/src directory. To run the
game from the root directory, type "java -jar javattt/src/ttt.jar".


===========
WEB VERSION
===========

From the root directory directory, run "ruby server/src/app.rb" to start the server. Server uses port 4567 and can be
begun by going to http://localhost:4567 in the browser. Run the two tests with rspec as "spec server/test." Browser DOM
test can be run by accessing http://localhost:4567/test after starting up the server.


=================
LIMELIGHT VERSION
=================

Run the game with "limelight open limelight/" and run the test via "spec limelight/spec/default_scene".