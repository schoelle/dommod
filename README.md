# DOMMOD Tool

A swiss army knife for working with Dominions 5 mod files from the command line.

# Call convention

```
dommod modfile.dm task [args ...]
```

# Supported Task

## check

No arguments.

Will try to parse the mod file. Afterwards, it will check if entities are defined more than once and if references are well defined. It will warn if there are entities
referenced that are not part of the mod or built-in to Dominions 5.

## dot

Optional argument of .dot filename (otherwise stdout will be used)

Produces a .dot file that can be used by [GraphViz](https://graphviz.org/) and similar tools to draw a dependency graph.

## output

Optional argument of .dm filename (otherwise stdout will be used)

Output the read-in module in .dm format, useful for converting from JSON to DM.

## json

Optional argument of .json filename (otherwise stdout will be used)

Output the read-in module in .json format, useful for converting from DM to JSON.

# Changelog

## 2021-02-09 - Version 0.1.0 

First release, includes the tasks: check, dot, output, json
