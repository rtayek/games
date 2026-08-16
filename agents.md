> Purpose: General AI agent behavior rules for any codebase.
> Scope: What agents may do freely, must ask before, and must never do.

# Agents

Guidelines for AI agents working in any project. Before doing anything,
agents MUST read `persona.md` and `human.md` alongside this file, plus any
other `.md` files at the project root and in folders you work in.

## Status

This document is normative. Where it uses **MUST / MUST NOT / SHOULD / MAY**, those words are used in the RFC sense.

## What agents may do freely

Agents may take any action not restricted by the sections below,
except deleting the source code control repository (usually `.git`).

## What agents must ask before doing

- Delete files permanently if they are not tracked by the source code control system (usually this is git).
- Renaming or moving files that are not tracked by the source code control system.
- Adding new dependencies.
- Making architectural changes (changes to module boundaries, public APIs, dependency structure, or overall design).
- Pushing the Git repository.

## What agents must never do

- Modify configuration files (`.env`, secrets, CI pipelines) without explicit instruction.
- Push to a Git repository without explicit instruction.

## What agents should do

- Ask when anything is unclear.
- Explain non-obvious changes.

## What agents may do

- Read access to the project folders and files.
- Commit to a local Git repository.
