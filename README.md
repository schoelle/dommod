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

# Changelog

## 2021-02-07

Added check and dot tasks.

## 2021-02-06 

Initial upload. Working first version of the parser, though there are still a many hole that need filling.
