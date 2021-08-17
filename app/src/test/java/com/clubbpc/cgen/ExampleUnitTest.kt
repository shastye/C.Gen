package com.clubbpc.cgen

import org.junit.Assert.assertEquals
import org.junit.Test

import java.util.Vector
import CharacterPackage.Character
import AttackPackage.Physical.Weapon
import CharacterPackage.Player
import Utility.Die

/*
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class CharacterClassUnitTests {
    @Test
    fun stats_constructorsWork() {
        val bs1 : Character.Base_Stats = Character.Base_Stats()
        val bs2 : Character.Base_Stats = Character.Base_Stats(7)
        val bs3 : Character.Base_Stats = Character.Base_Stats(
            1,2,3,4,5,6,7
        )

        //bs1
        assertEquals(10, bs1.Strength)
        assertEquals(10, bs1.Dexterity)
        assertEquals(10, bs1.Constitution)
        assertEquals(10, bs1.Intelligence)
        assertEquals(10, bs1.Wisdom)
        assertEquals(10, bs1.Charisma)
        assertEquals(10, bs1.Perception)
        //bs2
        assertEquals(7, bs2.Strength)
        assertEquals(7, bs2.Dexterity)
        assertEquals(7, bs2.Constitution)
        assertEquals(7, bs2.Intelligence)
        assertEquals(7, bs2.Wisdom)
        assertEquals(7, bs2.Charisma)
        assertEquals(7, bs2.Perception)
        //bs3
        assertEquals(1, bs3.Strength)
        assertEquals(2, bs3.Dexterity)
        assertEquals(3, bs3.Constitution)
        assertEquals(4, bs3.Intelligence)
        assertEquals(5, bs3.Wisdom)
        assertEquals(6, bs3.Charisma)
        assertEquals(7, bs3.Perception)
    }

    @Test
    fun stats_equalsWorks() {
        val bs : Character.Base_Stats = Character.Base_Stats(7)
        assertEquals(true, Character.Base_Stats(7).equals(bs))
        assertEquals(false, Character.Base_Stats(11).equals(bs))

        assertEquals(true, bs.equals(7))
        assertEquals(false, bs.equals(11))
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun stringConstructorWorks() {
        val char = Character("Guinevere")
        assertEquals("Guinevere", char._name)
    }

    @Test
    fun equalsWorks() {
        val char = Character("Guinevere")
        assertEquals(false, Character("Carl").equals(char))
        assertEquals(true, Character("Guinevere").equals(char))
    }

    @Test
    fun settersAndGettersWork() {

        val char = Character("Sophie")
        char._name = "Joe"
        char._race = Character.Race.DWARF
        char._health_points = 79
        char._alignment = Character.Alignment.LAWFUL_GOOD
        char._statistics = Character.Base_Stats(7)
        char._speed = 14
        char._armor_rating = 15
        char._total_hp_dice = 4
        char._hp_die = Die.d12


        assertEquals("Joe", char._name)
        assertEquals(Character.Race.DWARF, char._race)
        assertEquals(79, char._health_points)
        assertEquals(Character.Alignment.LAWFUL_GOOD, char._alignment)
        assertEquals(true, char._statistics.equals(7))
        assertEquals(14, char._speed)
        assertEquals(15, char._armor_rating)
        assertEquals(4, char._total_hp_dice)
        assertEquals(Die.d12, char._hp_die)

        char.add_weapon(Weapon.RAPIER)
        val temp : Vector<Weapon> = Vector<Weapon>(0)
        temp.add(Weapon.RAPIER)
        assertEquals(temp, char._weapons)
        assertEquals(temp.clear(), char.clear_weapons())
        assertEquals(true, char.add_weapon(Weapon.RAPIER))
        assertEquals(true, char.remove_weapon(Weapon.RAPIER))
        assertEquals(false, char.remove_weapon(Weapon.RAPIER))

        temp.add(Weapon.RAPIER)
        temp.add(Weapon.RAPIER)
        char.clear_weapons()
        char._weapons = temp
        assertEquals(temp, char._weapons)

        // need to still test attack once it's finished
    }

    @Test
    fun copyConstructorWorks() {
        val char1 = Character("Suzie")
        char1._hp_die = Die.d20
        char1._speed = 14
        char1.is_template_char = true

        val char2 = Character(char1)

        assertEquals(true, char1.equals(char2))
    }
}

class DieEnumClassUnitTest {
    @Test
    fun getIntWorks() {
        val d4 = Die.d4
        val d6 = Die.d6
        val d8 = Die.d8
        val d10 = Die.d10
        val d12 = Die.d12
        val d20 = Die.d20

        assertEquals(4, d4._int)
        assertEquals(6, d6._int)
        assertEquals(8, d8._int)
        assertEquals(10, d10._int)
        assertEquals(12, d12._int)
        assertEquals(20, d20._int)
    }
}

class PlayerClassUnitTests {
    @Test
    fun saves_constructorsWork() {
        val ds1 : Player.Death_Saves = Player.Death_Saves()
        val ds2 : Player.Death_Saves = Player.Death_Saves(7)
        val ds3 : Player.Death_Saves = Player.Death_Saves(2)
        val ds4 : Player.Death_Saves = Player.Death_Saves(1,2)

        //ds1
        assertEquals(0, ds1.successes)
        assertEquals(0, ds1.failures)
        //ds2
        assertEquals(3, ds2.successes)
        assertEquals(3, ds2.failures)
        //ds3
        assertEquals(2, ds3.successes)
        assertEquals(2, ds3.failures)
        //ds3
        assertEquals(1, ds4.successes)
        assertEquals(2, ds4.failures)
    }

    @Test
    fun saves_equalsWorks() {
        val bs : Player.Death_Saves = Player.Death_Saves(2)
        assertEquals(true, Player.Death_Saves(2).equals(bs))
        assertEquals(false, Character.Base_Stats(3) == (bs))

        assertEquals(true, bs.equals(2))
        assertEquals(false, bs.equals(1))
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun stringConstructorWorks() {
        val char = Player("Guinevere")
        assertEquals("Guinevere", char._name)
    }

    @Test
    fun equalsWorks() {
        val char = Player("Guinevere")
        assertEquals(false, Player("Carl").equals(char))
        assertEquals(true, Player("Guinevere").equals(char))

        char._hp_die = Die.d20
        assertEquals(false, Player("Guinevere").equals(char))

        char._hp_die = Die.d4
        char.add_failed_death_save()
        assertEquals(false, Player("Guinevere").equals(char))
    }

    @Test
    fun settersAndGettersWork() {
        val char = Player("Sophie")
        char._death_saves = Player.Death_Saves(1,2)
        char._total_hit_dice = 3
        char._hit_die = Die.d4

        assertEquals(true, char._death_saves.equals(Player.Death_Saves(1,2)))
        char.reset_death_saves()
        assertEquals(true, char._death_saves.equals(Player.Death_Saves(0,0)))
        char.add_succeeded_death_save()
        assertEquals(true, char._death_saves.equals(Player.Death_Saves(1,0)))
        char.add_failed_death_save()
        assertEquals(true, char._death_saves.equals(Player.Death_Saves(1,1)))
        assertEquals(3, char._total_hit_dice)
        assertEquals(Die.d4, char._hit_die)

        char.add_weapon(Weapon.RAPIER)
        val temp : Vector<Weapon> = Vector<Weapon>(0)
        temp.add(Weapon.RAPIER)
        assertEquals(temp, char._weapons)
        assertEquals(temp.clear(), char.clear_weapons())
        assertEquals(true, char.add_weapon(Weapon.RAPIER))
        assertEquals(true, char.remove_weapon(Weapon.RAPIER))
        assertEquals(false, char.remove_weapon(Weapon.RAPIER))

        temp.add(Weapon.RAPIER)
        temp.add(Weapon.RAPIER)
        char.clear_weapons()
        char._weapons = temp
        assertEquals(temp, char._weapons)

        // need to still test attack once it's finished
    }

    @Test
    fun copyConstructorWorks() {
        val char1 = Player("Suzie")
        char1._hit_die = Die.d20

        val char2 = Player(char1)

        assertEquals(true, char1.equals(char2))
    }
}