import scala.collection.mutable

object homework1:
  private val map: mutable.Map[String, Set[String]] = mutable.Map()
  private val macros: mutable.Map[String, expressions] = mutable.Map()

  enum expressions {
    case Value(input: String)
    case Name(input: String)
    case Delete(input: expressions)
    case Insert(input: expressions)
    case Union(input: Set[String])
    case Intersection(input: Set[String])
    case Difference(input: Set[String])
    case SymDifference(input: Set[String])
    case Cartesian(input1: Set[String], input2: Set[String])
    case Assign(op1: expressions, op2: expressions)
    case Print(op1: expressions)
    case Check(op1: expressions, op2: expressions)
    case Scope(op: expressions)
    case Macro(op1: expressions, op2: expressions)
    case RunMacro(op: expressions)


    def look_up: Boolean = {
      this match {
        case Check(op1, op2) => Look_Up_Helper(op1, op2)
        case _ => false
      }
    }

    def eval: String = {
      this match {
        case Value(input) => input
        case Name(input) => input
        case _ => "Unknown"
      }
    }

    def select_set(set: Set[String]) : Set[String] = {
      this match {
        case Insert(op) => set + op.eval
        case Delete(op) => set - op.eval
        case Union(set1) => set.union(set1)
        case Intersection(set1) => set.intersect(set1)
        case Difference(set1) => set.diff(set1)
        case SymDifference(set1) => SymDifferenceFunc(set, set1)
        case Scope(op2) => set + op2.eval
        case Print(op1) => map(op1.eval)
        case _ => Set()
      }
    }

    def eval_map(): Unit =  {
      this match {
        case Assign(op1, op2) => AddMap(op1, op2)
        case Macro(op1, op2) => MacroMap(op1, op2)
        case RunMacro(op1) => RunMacros(op1)
        case _ =>
      }
    }

    def find: Set[(String, String)] = {
      this match {
        case Cartesian(op1, op2) => CartFunc(op1, op2)
        case _ => Set()
      }
    }

    private def SymDifferenceFunc(set1: Set[String], set2: Set[String]): Set[String] = {
      val set: mutable.Set[String] = mutable.Set()
      set1.foreach(x =>
        if (!set2.contains(x)) {
          set.add(x)
        }
      )

      set2.foreach(x =>
        if (!set1.contains(x)) {
          set.add(x)
        }
      )

      set.toSet
    }

    private def CartFunc(set1: Set[String], set2: Set[String]): Set[(String, String)] = {
      val set: mutable.Set[(String, String)] = mutable.Set()
      set1.foreach(i => {
        set2.foreach(j => {
          set.add((i, j))
        })
      })

      set.toSet
    }

    private def AddMap(op1: expressions, op2: expressions): Unit = {
      val temp = op1.eval
      if (!map.contains(temp))
        map(temp) = Set()

      map(temp) = op2.select_set(map(temp))
    }

    private def MacroMap(op1: expressions, op2: expressions): Unit = {
      val temp = op1.eval

      if (!macros.contains(temp))
        macros(temp) = null

      macros(temp) = op2
    }

    private def RunMacros(op: expressions): Unit = {
      val temp = op.eval

      if (!macros.contains(temp)) return Set()

      val command = macros(temp)
      val exp = command.select_set(map(temp))
      map(temp) = exp
    }

    private def Look_Up_Helper(op1: expressions, op2: expressions): Boolean = {
      val op1_val = op1.eval

      if (map.contains(op1_val)) {
        val temp = map(op1_val)
        if (temp.contains(op2.eval)) {
          return true
        }
      }
      false
    }
  }


  @main def test(): Unit =
    import expressions.*
    /*val set1 = Set("1", "2")
    val set2 = Set("3", "4")
    val set_11 = Insert(Value("5")).select_set(set1)
    val set_22 = Insert(Value("5")).select_set(set2)
    val union = Union(set_11).select_set(set_22)
    val intersect = Intersection(set_11).select_set(set_22)
    val diff = Difference(set_11).select_set(set_22)
    val sym_diff = SymDifference(set_11).select_set(set_22)
    val cart = Cartesian(set_11, set_22).find*/

    Assign(Name("hm"), Insert(Value("5"))).eval_map()
    Assign(Name("hm"), Insert(Value("3"))).eval_map()
    println(Print(Name("hm")).select_set(null))
    Assign(Name("hm"), Delete(Value("2"))).eval_map()
    println(Print(Name("hm")).select_set(null))
    println(Check(Name("hm"), Value("3")).look_up)

