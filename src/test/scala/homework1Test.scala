// Omar Walweel
// February 10, 2022
// Homework 1

import homework1.expressions
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class homework1Test extends AnyFlatSpec with Matchers {
  behavior of "my first language for set theory operations"
  import expressions.*

  // This is testing for the Assign, Insert, Value, Name, Delete, and Check expressions
  it should "Create a map that permanently adds or deletes sets as values and an identifier key string as the key." +
    "It will check if a key or value exists in the map and return true or false respectively." in {
    Assign(Name("set1"), Insert(Value("5"))).eval_map
    Assign(Name("set1"), Insert(Value("3"))).eval_map
    Assign(Name("set1"), Insert(Value("2"))).eval_map
    Check(Name("set1"), Value("3")).look_up shouldBe true
    Check(Name("set1"), Value("5")).look_up shouldBe true
    Check(Name("set1"), Value("2")).look_up shouldBe true
    Assign(Name("set1"), Delete(Value("2"))).eval_map
    Check(Name("set1"), Value("2")).look_up shouldBe false
  }

  // This is testing for the set operations and Clear
  it should "This will test the set operations like union, intersection, etc." in {
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
    Union(Name("set1"), Name("set2")).eval_set shouldBe Set("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    Intersection(Name("set1"), Name("set2")).eval_set shouldBe Set("1", "2", "3", "5")
    Difference(Name("set1"), Name("set2")).eval_set shouldBe Set("4", "6", "7")
    SymDifference(Name("set1"), Name("set2")).eval_set shouldBe Set("4", "6", "7", "8", "9", "10")
    Clear().eval_map
    Assign(Name("set1"), Insert(Value(1))).eval_map
    Assign(Name("set1"), Insert(Value(2))).eval_map
    Assign(Name("set1"), Insert(Value(3))).eval_map
    Assign(Name("set2"), Insert(Value(4))).eval_map
    Assign(Name("set2"), Insert(Value(5))).eval_map
    Assign(Name("set2"), Insert(Value(6))).eval_map
    Cartesian(Name("set1"), Name("set2")).eval_set shouldBe Set((3,4), (2,5), (2,6), (1,4), (1,6), (2,4), (1,5), (3,6), (3,5))
  }

  // This is testing for macros
  it should "Test macros" in {
    Assign(Name("set1"), Insert(Value("1"))).eval_map
    Assign(Name("set1"), Insert(Value("2"))).eval_map
    Assign(Name("set1"), Insert(Value("3"))).eval_map
    Macro(Name("set1"), Delete(Value("3"))).eval_map
    Check(Name("set1"), Value("3")).look_up shouldBe true
    Assign(Name("set1"), RunMacro(Name("set1"))).eval_map
    Check(Name("set1"), Value("3")).look_up shouldBe false
  }

  // This is testing for scopes
  it should "Test scopes" in {
    val map = Scope(Name("hm"), Insert(Value("4"))).eval_map
    map("hm") shouldBe Set("4")
    Scope(Name("hm"), Insert(Value("3"))).eval_map
    Scope(Name("hm"), Insert(Value("2"))).eval_map
    Scope(Name("hm"), Insert(Value("1"))).eval_map
    map("hm") shouldBe Set("4")
  }
}