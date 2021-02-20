# DOMMOD Tool

A swiss army knife for working with Dominions 5 mod files from the command line.

# Call convention

```
dommod modfile.dm task [args ...]
```

# Supported Task

## check

* No arguments

Will try to parse the mod file and then checks if entities are defined more than once and if references are well defined. It will warn if there are entities
referenced that are not part of the mod or built-in to Dominions 5.

### Example

```
$ ./dommod PretendersEnhancedv3_1.dm check
  WARNING: External reference detected: MONSTER(-5160)
  WARNING: External reference detected: NAMETYPE(172)
Total of 0 errors and 2 warnings found.
```

## dot

Optional argument of .dot filename (otherwise stdout will be used)

Produces a .dot file that can be used by [GraphViz](https://graphviz.org/) and similar tools to draw a dependency graph.

### Example

```
$ ./dommod nationgen_test2.dm dot output.dot
$ neato -Tpng output.dot -o output.png
$ xdg-open output.png 
```

## json

Optional argument of .json filename (otherwise stdout will be used)

Output the read-in module in .json format, useful for converting from DM to JSON.

### Example

```
$ ./dommod nationgen_test2.dm json output.json
```

## output

Optional argument of .dm filename (otherwise stdout will be used)

Output the read-in module in .dm format, useful for converting from JSON to DM.

### Example

```
$ ./dommod nationgen_test2.json output output.dm
```

## info

No argument

Prints some statistical information about the mod.

### Example

```
$ ./dommod PretendersEnhancedv3_1.dm info
Mod Name        : Pretenders Enhanced v3.1
Icon            : ./ExtraPretenders/PElogo.tga
Version         : 3.1
Dom Version     : not specified

Description:
Rebalances all Pretenders to widen the viable chassis options, and adds over 100
new Pretender options. Monsters lose some magic but get physical buffs, Titans
are buffed and made cheaper, whilst rainbows and immobiles are mostly in line
with vanilla.

Commands        : 11503
All Definitions : 643
Weapon          : 26
Monster         : 512
Spell           : 3
Item            : 1
Nation          : 90
Event           : 11
```

## slicenation

First argument is the nation id to extract, send is an output dm file

Tries to extract a single nation from a mod. Be aware that this command removes all events.

### Example

```
$ ./dommod flex3_parsable.dm slicenation 159 output.dm
Extracting nation 159: Ugheim
National Spell: Summon Valkyries
...
National Spell: Summon Dwarf of the Four Directions
Removing: Weapon(800): Bite
...
Removing: Nation(157): Yortsu
Keeping: Spell: Summon Valkyries
...
Keeping: Site(1560): Charred Castle
Keeping: Nation(159): Ugheim
```

## usedids

Argument is an optional output file, otherwise output is printed.

Extracts a list of all numeric IDs used in a mod and writes them to a text file. Can be combined with `avoid` to make
two mods work together without redefining each other's IDs.

### Example

```
$ ./dommod nationgen_test1.dm usedids
MONSTER(3521) -- Master of the Invincible Covens
MONSTER(3511) -- Djedrisian Champion
...
WEAPON(800) -- Kick
MONSTER(3502) -- Djedrisian Champion
MONSTER(3514) -- Scout
MONSTER(3504) -- Djedrisian Zotz Slave Axeman
MONSTER(3516) -- General
$ ./dommod nationgen_test1.dm usedids ids.txt
```

## avoid

First argument is an ids.txt file as created by `usedids`, second argument is an output mod file name.

Creates a new mod where all IDs that are contained in the ids.txt file have been renamed to new, unused ones.

```
$ ./dommod nationgen_test2.dm avoid ids.txt output.dm
Chaning ID from MONSTER(3520) to MONSTER(3543)
Chaning ID from MONSTER(3500) to MONSTER(3544)
Chaning ID from MONSTER(3501) to MONSTER(3545)
Chaning ID from MONSTER(3517) to MONSTER(3546)
Chaning ID from MONSTER(3507) to MONSTER(3547)
Chaning ID from WEAPON(802) to WEAPON(898)
Chaning ID from MONSTER(3508) to MONSTER(3548)
Chaning ID from WEAPON(803) to WEAPON(899)
Chaning ID from MONSTER(3509) to MONSTER(3549)
Chaning ID from WEAPON(800) to WEAPON(900)
Chaning ID from MONSTER(3524) to MONSTER(3550)
Chaning ID from MONSTER(3502) to MONSTER(3551)
Chaning ID from MONSTER(3513) to MONSTER(3552)
Chaning ID from WEAPON(801) to WEAPON(910)
Chaning ID from MONSTER(3503) to MONSTER(3553)
Chaning ID from MONSTER(3514) to MONSTER(3554)
Chaning ID from MONSTER(3515) to MONSTER(3555)
Chaning ID from MONSTER(3516) to MONSTER(3556)
Chaning ID from NATION(120) to NATION(121)
Chaning ID from SITE(1501) to SITE(1502)
Chaning ID from SITE(1500) to SITE(1503)
Chaning monster tag 1002 to 1007
```

# Changelog

## 2021-02-20 - Version 0.2.0

* Fixed calculating line numbers
* Print line numbers of duplicate definitions check
* NEW COMMAND: info
* NEW COMMAND: slicenation
* Some support for event definitions
* NEW COMMAND: userids
* NEW COMMAND: avoid
* Improved 'dommod' shell wrapper

## 2021-02-09 - Version 0.1.0 

First release, includes the tasks: check, dot, output, json
