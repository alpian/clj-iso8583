# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

## [0.5.0] - 2023-04-10

### Changed

- include field name in error output when field is too short ([commit #ac6c3858f0](https://github.com/oakmac/com.oakmac.iso8583/commit/ac6c3858f0ea9b36ddaca352b6e8f7c7afaafcb1))
- minor: whitespace / formatting changes

## [0.4.0] - 2022-12-06

- added unit tests for `com.oakmac.iso8583.binary/ubyte`
- remove Java interop code for simpler distribution

## [0.3.0] - 2022-12-02

- fix syntax error in let block

## [0.2.0] - 2022-12-02

- fix [clj-kondo] warnings
- replace all `use` with `require`
- replace [Midje] with [clojure.test] for testing
- use `com.oakmac.iso8583` for namespaces
- update Clojure to version 1.11.1

[clj-kondo]:https://github.com/clj-kondo/clj-kondo
[Midje]:https://github.com/marick/Midje
[clojure.test]:https://clojure.github.io/clojure/clojure.test-api.html

## [0.1.0]

- [alpian/clj-iso8583] had `"0.1"` as a version listed in the `project.clj` file as of commit `3e41ce5790711a9d50961d6a48bced150a95391c`

[alpian/clj-iso8583]:https://github.com/alpian/clj-iso8583

[Unreleased]: https://github.com/oakmac/com.oakmac.iso8583/compare/v0.5.0...HEAD
[0.5.0]: https://github.com/oakmac/com.oakmac.iso8583/releases/tag/v0.5.0
[0.4.0]: https://github.com/oakmac/com.oakmac.iso8583/releases/tag/v0.4.0
[0.3.0]: https://github.com/oakmac/com.oakmac.iso8583/releases/tag/v0.3.0
[0.2.0]: https://github.com/oakmac/com.oakmac.iso8583/releases/tag/v0.2.0
[0.1.0]: https://github.com/alpian/clj-iso8583/tree/3e41ce5790711a9d50961d6a48bced150a95391c
