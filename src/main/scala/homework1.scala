import scala.collection.immutable.HashSet

object homework1:
  enum expressions {
    case Value(input: String)
    case Delete(input: expressions)
    case Add(input: expressions)
    case Union(input: HashSet[String])
    case Intersection(input: HashSet[String])
    case Difference(input: HashSet[String])
    case SymDifference(input: HashSet[String])

    def eval: String = {
      this match {
        case Value(input) => input
      }
    }

    def select_set(set: HashSet[String]) : HashSet[String] = {
      this match {
        case Add(op) => set + op.eval
        case Delete(op) => set - op.eval
        case Union(set1) => set.union(set1)
        case Intersection(set1) => set.intersect(set1)
        case Difference(set1) => set.diff(set1)
        case SymDifference(set1) => set ++ set1
      }
    }

    private def SymDifferenceFunc(set1: HashSet[String], set2: HashSet[String]): HashSet[String] = {
      set1.foreach(x =>
        if (!set2.contains(x)) {
      })

      HashSet("1")

    }
  }


  @main def test(): Unit =
    import expressions.*
    val set1 = HashSet("1", "2")
    val set2 = HashSet("3", "4")
    val set_11 = Add(Value("5")).select_set(set1)
    val set_22 = Add(Value("5")).select_set(set2)
    val union = Union(set_11).select_set(set_22)
    val intersect = Intersection(set_11).select_set(set_22)
    val diff = Difference(set_11).select_set(set_22)
    val sym_diff = SymDifference(set_11).select_set(set_22)

    println(sym_diff)

