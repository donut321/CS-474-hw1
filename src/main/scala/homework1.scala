// Omar Walweel
// February 10, 2022
// Homework 1

import scala.collection.mutable

object homework1:
  // Global maps to store the macros and the permenant map
  private val map: mutable.Map[Any, Set[Any]] = mutable.Map()
  private val macros: mutable.Map[Any, expressions] = mutable.Map()

  enum expressions {
    // This will be the scope expression. If the user decides to not
    // save the scope that is returned, it will be destroyed at the end
    private val scope_map: mutable.Map[Any, Set[Any]] = mutable.Map()

    // Define the expressions
    case Value(input: Any)
    case Name(input: Any)
    case Delete(input: expressions)
    case Clear()
    case Insert(input: expressions)
    case Union(input1: expressions, input2: expressions)
    case Intersection(input1: expressions, input2: expressions)
    case Difference(input1: expressions, input2: expressions)
    case SymDifference(input1: expressions, input2: expressions)
    case Cartesian(input1: expressions, input2: expressions)
    case Assign(op1: expressions, op2: expressions)
    case Print(op1: expressions)  // This is just for testing in main
    case Check(op1: expressions, op2: expressions)
    case Scope(op1: expressions, op2: expressions)
    case Macro(op1: expressions, op2: expressions)
    case RunMacro(op: expressions)
    case GetSet(name: expressions)

    // This will return if the value is in the map or not
    // If none, return false
    def look_up: Boolean = {
      this match {
        case Check(op1, op2) => Look_Up_Helper(op1, op2)
        case _ => false
      }
    }

    // This will take the case Value or Name, and return the value which could be string or int.
    // If none, return unknown
    def eval: Any = {
      this match {
        case Value(input) => input
        case Name(input) => input
        case _ => "Unknown"
      }
    }

    // This takes in a string input (which is the set that is assigned to the key in the map)
    // and either returns a new set with the deleted value or adds it. It also is responsible
    // for running the macro operation. If no case, return empty set
    def select_set(set: Set[Any]) : Set[Any] = {
      this match {
        case Insert(op) => set + op.eval
        case Delete(op) => set - op.eval
        case RunMacro(op1) => RunMacros(op1)
        case _ => Set()
      }
    }

    // This will be responsible for running the set operations like unions and intersection functions.
    // If none, return empty set.
    def eval_set: Any = {
      this match {
        case Union(set1, set2) => map(set1.eval).union(map(set2.eval))
        case Intersection(set1, set2) => map(set1.eval).intersect(map(set2.eval))
        case Difference(set1, set2) => map(set1.eval).diff(map(set2.eval))
        case SymDifference(set1, set2) => SymDifferenceFunc(map(set1.eval), map(set2.eval))
        case Cartesian(op1, op2) => CartFunc(op1, op2)
        case Print(op1) => map(op1.eval)  //This is for testing purposes
        case _ => Set()
      }
    }

    // This will either clear the map, or add to the map. But the map
    // here is decided by the user. If the expression is scope, it
    // will be added to the scope map that will be destroyed at the end.
    // Else, it will add or delete from the permanent map.
    // If none, return empty map
    def eval_map: mutable.Map[Any, Set[Any]] =  {
      this match {
        case Assign(op1, op2) => AddMap(op1, op2, map)
        case Clear() => Clear()
        case Scope(op1, op2) => AddMap(op1, op2, scope_map)
        case _ => mutable.Map()
      }
    }

    // This is responsible for calling the operation that will store tha macro in map
    // If none, return empty map
    def eval_macro: mutable.Map[Any, expressions] =  {
      this match {
        case Macro(op1, op2) => MacroMap(op1, op2)
        case _ => mutable.Map()
      }
    }

    // This will clear the permanent map and return it
    private def Clear(): mutable.Map[Any, Set[Any]] = {
      map.clear()
      map
    }

    // This is responsible for the Symmetric difference operation between two sets
    // Make a new mutable set. For each value in set 1, add it to the new set.
    // Now for each value in set 2, add it to new set.
    private def SymDifferenceFunc(set1: Set[Any], set2: Set[Any]): Set[Any] = {
      val set: mutable.Set[Any] = mutable.Set()

      // Add all from set 1
      set1.foreach(x =>
        if (!set2.contains(x)) {
          set.add(x)
        }
      )

      // Add all from set 2
      set2.foreach(x =>
        if (!set1.contains(x)) {
          set.add(x)
        }
      )

      // Return the set
      set.toSet
    }

    // This is the cartesian helper function. It will take both sets from the map
    // then for each value in set 1, go in set 2. For each value in set 2, add i and j
    // together in a pain to a new mutable set
    private def CartFunc(set1: expressions, set2: expressions): Set[(Any, Any)] = {
      val set: mutable.Set[(Any, Any)] = mutable.Set()
      val set1_temp = map(set1.eval)
      val set2_temp = map(set2.eval)

      // For each set 1, go in set 2. Add i and j together as a pair in the set
      set1_temp.foreach(i => {
        set2_temp.foreach(j => {
          set.add((i, j))
        })
      })

      // Return the set
      set.toSet
    }

    // This is a helper function for the permanent map or the scope map.
    private def AddMap(op1: expressions, op2: expressions, map: mutable.Map[Any, Set[Any]]): mutable.Map[Any, Set[Any]] = {
      val temp = op1.eval

      // If key doesn't exist, create new set and assign it to it.
      if (!map.contains(temp))
        map(temp) = Set()

      // Select the key in the map and call the operation in the set selection (insert, delete, or run macro)
      map(temp) = op2.select_set(map(temp))

      // Return map
      map
    }

    // This is responsible to help store the expressions in the macro map
    private def MacroMap(op1: expressions, op2: expressions): mutable.Map[Any, expressions] = {
      val temp = op1.eval

      // If key doesn't exist, create the key and assign it to null
      if (!macros.contains(temp))
        macros(temp) = null

      // Assign the key to the expression and return the map
      macros(temp) = op2
      macros
    }

    // This is responsible to run the macro expression. It will first parse the command
    // then go again to the set choices and pick either insert or delete
    private def RunMacros(op: expressions): Set[Any] = {
      val temp = op.eval

      // If key doesn't exist, return empty set
      if (!macros.contains(temp)) return Set()

      // If it does exist, store the command and assign the key to null in the map.
      // Then call the command in the set operations.
      val command = macros(temp)
      macros(temp) = null
      command.select_set(map(temp))
    }

    // This is a helper function for the check expression. If the key or value dont exist, return false
    // Else return true
    private def Look_Up_Helper(op1: expressions, op2: expressions): Boolean = {
      val op1_val = op1.eval

      // Check if key exists.
      if (map.contains(op1_val)) {
        // Check if map contains the value in the set that is assigned to the key
        if (map(op1_val).contains(op2.eval)) {
          return true
        }
      }

      // If one of these conditions dont satisfy, return false
      false
    }
  }


  @main def test(): Unit =
    import expressions.*

    // Ignore these. They're just to test while coding.
    /*
    Assign(Name("set1"), Insert(Value("1"))).eval_map
    Assign(Name("set1"), Insert(Value("2"))).eval_map
    Assign(Name("set1"), Insert(Value("3"))).eval_map
    Assign(Name("set1"), Insert(Value("4"))).eval_map
    Assign(Name("set1"), Insert(Value("5"))).eval_map
    Assign(Name("set1"), Insert(Value("6"))).eval_map
    Assign(Name("set1"), Insert(Value("7"))).eval_map
    Assign(Name("set2"), Insert(Value("8"))).eval_map
    Assign(Name("set2"), Insert(Value("1"))).eval_map
    Assign(Name("set2"), Insert(Value("2"))).eval_map
    Assign(Name("set2"), Insert(Value("10"))).eval_map
    Assign(Name("set2"), Insert(Value("3"))).eval_map
    Assign(Name("set2"), Insert(Value("5"))).eval_map
    Assign(Name("set2"), Insert(Value("9"))).eval_map
    Clear().eval_map
    Assign(Name("set1"), Insert(Value(1))).eval_map
    Assign(Name("set1"), Insert(Value(2))).eval_map
    Assign(Name("set1"), Insert(Value(3))).eval_map
    Assign(Name("set2"), Insert(Value(4))).eval_map
    Assign(Name("set2"), Insert(Value(5))).eval_map
    Assign(Name("set2"), Insert(Value(6))).eval_map
    //println(Check(Name("set1"), Value("2")).look_up)
    println(Check(Name("set1"), Value(1)).look_up)
    println(Macro(Name("set1"), Delete(Value(1))).eval_macro)
    println(Print(Name("set1")).eval_set)
    Assign(Name("set1"), RunMacro(Name("set1"))).eval_map
    println(Check(Name("set1"), Value(1)).look_up)
    println(Macro(Name("set1"), Delete(Value(2))).eval_macro)
    Assign(Name("set1"), RunMacro(Name("set1"))).eval_map
    println(Print(Name("set1")).eval_set)
    println(Check(Name("set1"), Value(2)).look_up)*/

    /*println(Cartesian(GetSet(Name("set1")).get_set, GetSet(Name("set2")).get_set).find)
    val map = Scope(Name("hm"), Insert(Value("4"))).scope
    println(map)*/

