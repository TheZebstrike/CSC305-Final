Step 1: Click and draw a square; it has a name (Name01/2/3/... default for example)
	- One of previous labs implemented this, start with that code
 
Step 2: User can change name of the square
	- User clicks on box, input box pops up, can edit name, then user can click OK or Cancel.
 
Step 3: Allow drag and drop movement for the box
	- Make sure collision works, two squares shouldn't be able to be on top of each other
	- Or consider drawing a grid for boxes to snap into when dragged
 
Step 4a: Right click to display a popup menu
	- Options are observer, observable, singleton, decoration, decoratable, chain member (chain of responsibility), strategy, factory, product
 
Step 4b: Decorate the box according to the selection
	- Make some custom decoration, make it intuitive for the user.
	- Text is fine, but images or drawings are encouraged (implement text first, then we can do drawings at the end if we want)
	- Several decorations can be applied to the same box!
 
Step 5: Allow user to connect decorations with lines
	- Lines/connections should be curved lines
 
Step 6: Allow user to connect boxes (classes)
	- Menu should have File and "Box Connector" buttons
	- Box Connector has these options: agregation, composition, association, inheritance, realization (different types of connections between classes)
	- Use https://github.com/CSC3100/Patterns/tree/main/ChallengeCities code for help on steps 1-6
	- Lines/connections should be labeled somehow in the diagram. Consider putting small labels on line or using different colors for each type of connection.
 
Step 7: Use JTabbedPane to create "Draw Area" and "Code" tabs under the main Menu
	- Draw Area is where the class boxes and connections will be drawn by the user
	- Code tab will look like this:
		- Left side of panel has file structure layout (src -> x.java, y.java, z.java, etc)
		- Right side of panel will show the code for the class that is clicked on by the user. (highlight/make darker the class on the left side of panel when user clicks it)
	- Code for tabs was used in a program provided in week 1-2 about libraries!
 
Step 8: Generate Text (Code in Java)
	- Add Tools button to main Menu, the button will have a Run option when clicked on. The Run button will generate the code based on what the user has put in the Draw Area UML diagram. It should generate each of the classes with the appropriate code for the standard solutions/decorations applied to each class/box.
	- Figure out what code should be generated automatically for each pattern applied to the classes, as well as for the connections that are made between the classes/pattens!
 
Step 9: Add options to File button in main Menu
	- New (clears Draw Area and generated code), Open (user can open a saved file), Save As (save project/draw area/generated code to a new file), and Save (save project/draw area/generated code to an existing file)
	- Figure out what to save to be able to reload both the diagram and code.
 
(BONUS) Step 10: Add a Help button to the main Menu (at the end), should have an option called "About" when clicked
	- Show a popup that has project name, year, and author
