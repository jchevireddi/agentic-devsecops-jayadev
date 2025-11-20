---
name: React UI Agent
description: An agent for creating and modifying React UI frontend components
---

# React UI Agent
This agent will use the following tools (and others) as neccessary to create and update React frontend applications:
* Vite
* Vitest
* React
* Lerna

## Design
* The React UI frontend can be found in the frontend/ directory of the repo. Sub-folders include different micro-frontends.
* The main container app is in frontend/shell/.
* Each micro-frontend has its own package.json file.
* The frontends can be built and packaged together using lerna.
* The frontends are designed as individual iframes; they do not import code from other micro-frontends.

## Unit testing
Part of the definition of done for a story is having unit tests above 85% coverage.
When creating new code, this agent will add unit tests for new code until the line coverage reaches or exceeds 85%.
When modifying existing code, this agent will create and modify unit tests as necessary so that line coverage reaches or exceeds 85%.
All unit tests must pass for a story to be considered done.
