- Omar Walweel
- February 10, 2022
- Homework 1

The language used in this homework is very similar to how the code in the example description is supposed to behave, I added extra features though.
I have the following expressions: Value, Name, Delete, Assign, Insert, Clear, Union, Intersection, Difference, Symmetrical Difference, Cartesian, Print, Check, Scope, Macro, and RunMacro.

# Rules of the language
- Each expression needs to run with a .function, but only the outer ones need that, as the inner get run by the outer ones. (The names of the functions and what their parameters is after this)
- You cannot write the expression or expressions that function similarly inside themselves. For example, Name(Value("something")) will result with a syntax error. Same with Insert(Delete(Name("something"))).
- Name holds a string ONLY, but Value can hold any value or object. Thus, Name is used to define the key in the scope, while Value inside expressions like Insert or Delete to tell the language what to do with it. However, using Value as the key will not cause an error.
- Assign, Scope, and Macro DO NOT allow anything other than Value or Name to be their first parameter.
- When making a macro, you MUST call the macro to a similar name of the global map.


# Expressions positioning
Expressions are using types, and calculating what comes below them. So here is a rundown of can be used where.
First on the list is what comes first and nothing can come before. Going down is what goes in the point above it. For example, Insert come below Assign, so Insert can be used inside Assign, but not the other way around.
- Maps: Scope, Assign, and Macro are not allowed to be inside each other
- Sets: Insert, Delete, and RunMacro cannot be inside each other
- Normal types: Value and Name cannot be inside each other

You might wonder, there are expressions that are not there. Yes, and they are Union, Intersection, Difference, SymDifference, Cartesian, Check, Clear, and Print.
They are a special case. Aside from Clear, they ONLY accept Name or Value to do their functionality. Clear however, has no parameters and does not take any input.

# How to run each expression
As mentioned above, each expression needs to be called by its function to run, but as long as there is an outer expression, it gets called automatically buy it.

- Check can be called by ending the expression with .lookup : Will return boolean
- Value and Name can be called by ending the expression with .eval : Will return Any
- Insert, Delete, and RunMacro can be called by ending the expression with .select_set(specify the set) : Will return Set[Any]
- Union, Intersection, Difference, SymDifference, Cartesian, and Print can be called by ending the expression with .eval_set : Will return Any
- Assign, Clear, and Scope can be called by ending the expression with .eval_map : Will return mutable.Map[Any, Set[Any]]
- Macro can be called by ending the expression with .eval_macro : mutable.Map[Any, expressions]

# Parameters
I talked about what type of expression can hold what. Now I will talk about their parameter numbers
- Clear does not have any parameters
- Value, Name, Delete, Insert, Print, and RunMacro can hold exactly 1 parameters
- Union, Intersection, Difference, SymDifference, Cartesian, Assign, Check, Scope, and Macro can hold exactly 2 parameters


# How the language was made
The implementation is very simple. Basically, There is a global map that is responsible for storing sets and assigning them to keys.
This map is called "map". Everything except Scope and Macro will deal with this map. Assign will parse the name in the first parameter and check if it exists.
If yes, it takes the set, and adds or deletes the coming value from insert or delete to the current set. If not, it creates a new key and set and starts pushing or deleting there.
Scope is similar to Assign. Except, the user needs to save the result of Scope into variable. Because after the expression is done, the scope will be destroyed.
Macro has a map too. But instead of holding sets, it holds expressions. So when Assign or Scope call RunMacro, it will look for the key name in the macros map, and the outer function will now treat it as a normal insert or delete expression.
The key will be assigned to null after that. Union, Intersection, and Difference use Scala functions for sets. SymDifference loops through set1 and adds all its values to a new set. Then does the same for the second set. Then returns the set.
Cartesian will loop through the first set and at each iteration, it will loop inside the second set and make a pair and push it to a set that consists of (value from set1, value from set 2)

# How to use the language
In main, you can type your commands there and run it. Or you could also
type bst run to run the tests. To find an example on how to write your code, see "homework1Test", where it provides you with an example of each expression.