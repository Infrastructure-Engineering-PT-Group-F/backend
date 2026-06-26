# Changelog

## [1.5.0](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/compare/v1.4.0...v1.5.0) (2026-06-26)


### Features

* [#39](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/39) move management api to own port ([01d989e](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/01d989e9d56440a0d9faf6c99acf6701bd8e5186))

## [1.4.0](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/compare/v1.3.1...v1.4.0) (2026-06-25)


### Features

* [#35](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/35) replace chart Ingress with Gateway API HTTPRoute ([1c14583](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/1c14583d4675e57026d407bd48e61500582869ab))


### Bug Fixes

* [#31](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/31) set chart appVersion to v1.3.1 ([5ef371f](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/5ef371f5595b8ac96c298bdd21dedbac0adbf815))

## [1.3.1](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/compare/v1.3.0...v1.3.1) (2026-06-23)


### Bug Fixes

* [#11](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/11) add default resource requests and limits ([1da46de](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/1da46deb5b5776365c3303101e126021ad3ff5c9))
* [#11](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/11) add startup probe delays for hardened backend ([98112ed](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/98112edbd8b8adf49b5eb994b78c207e3a2f1fdb))
* [#11](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/11) harden backend container runtime ([818e8d9](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/818e8d9f54f74506698e693a7bdfe83cd513fd6c))
* [#11](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/11) use startup probe for backend boot ([da99ca0](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/da99ca062893eefd68966c695cbab2b80e880dbf))

## [1.3.0](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/compare/v1.2.0...v1.3.0) (2026-06-21)


### Features

* [#10](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/10) make backend Helm chart platform-ready ([3dec575](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/3dec57567e5e0e21736a51ebece6a01d7f98907e))


### Bug Fixes

* [#10](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/10) address platform readiness review comments ([d9a1139](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/d9a1139405a2f7a0eb33f480a441909e5f242e58))

## [1.2.0](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/compare/v1.1.0...v1.2.0) (2026-06-20)


### Features

* [#8](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/8) adapt helm charts ([1efbfeb](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/1efbfeb20bc1e230d7ecc1b79517bf90d658e497))
* [#8](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/8) switch persistence to PostgreSQL via env-selected profile ([64c78d6](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/64c78d659da523b20e425e69c239177c3ca9d7f7))

## [1.1.0](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/compare/v1.0.0...v1.1.0) (2026-06-18)


### Features

* [#12](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/12) adding helm chart releasing and aligning verification with frontend ([5d6e5a8](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/5d6e5a8fa14054f7ebbbf8ae9953d2dae6d1ad75))
* [#13](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/13) expose metar proxy endpoint ([ecf83a4](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/ecf83a40d90622323dc82e91410030b3f5315ceb))
* [#13](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/13) proxy AVWX METAR server-side via MetarService ([43ed944](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/43ed944508a3fc6c65c75f2a65e0c3af426257fc))
* [#3](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/3) import and adapt weather-app backend reference application ([24244e5](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/24244e5cbc38f0099fcfd3429fb8417d7f016633))
* [#9](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/9) create release please config and setting pushing images to GHCR ([974857a](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/974857aedef6694b87c1a9008bbe2855cf9891c6))


### Bug Fixes

* [#18](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/18) use simple release-type with generic build.gradle updater ([9ac8131](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/9ac81314f152130dc33e2d051a6781917cda7355))
* [#21](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/21) ignore release-please release commits in commitlint ([6954370](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/69543706be2af8fa36971a6f1dad86c8cc32a607))


### Miscellaneous Chores

* [#2](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/2) add local commit message hook for commit rules ([2e47613](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/commit/2e4761329b1b40c0d887059621348f55aec43e2c))
