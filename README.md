### Hex Casting Extensions aka. Hextensions aka. Another String Addon.
Mod intended to add minimum amount of changes which Hex Casting lacks in author's opinion.

Feel free to copy code from this project (which there is not much of right now) or continue it, if it never gets published.

- Strings are the only added iota type.
- Name strings were chosen instead of identifiers, because:
  - they are more useful lexically
  - they are nothing more than data (cross-world or iota forgery gives no benefit)

Untranslated spells need more concept work / under question.

Currently held ideas:
- Other hand slot being main slot for interaction (matches with item picking behaviour)
- Spell for swapping items with other hand
  - `holder, slot ->` could be a common signature
  - `??? -> item (entity)` would be useful and concise (most item spells take item entity)
  - Swap `holder, slot ->` Swaps item from holder with other hand.
  - Throw `pos -> item` Drops other hand item at coordinate.
  - Dispenser-like spell may be useful, but better be merged into general interaction spell (Hexical's Prestigination)   