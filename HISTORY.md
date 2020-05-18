# [4.0.0](https://github.com/hexonet/java-sdk/compare/v3.2.0...v4.0.0) (2020-05-18)


### Features

* **responsetranslator:** added initial version coming with rewrite/restructuring from scratch ([a953e69](https://github.com/hexonet/java-sdk/commit/a953e69e32a4e0c32286b33b36c726425c9e9f97))


### BREAKING CHANGES

* **responsetranslator:** Downward incompatible restructuring (Merge of Response/ResponseTemplate, Rewrite
ResponseTemplateManager) and introducing ResponseTranslator

# [3.2.0](https://github.com/hexonet/java-sdk/compare/v3.1.0...v3.2.0) (2020-04-28)


### Features

* **logger:** possibility to override debug mode's default logging mechanism. See README.md ([b878191](https://github.com/hexonet/java-sdk/commit/b878191142d476a0d46fc86b9a1385e3572bdd56))

# [3.1.0](https://github.com/hexonet/java-sdk/compare/v3.0.0...v3.1.0) (2020-04-28)


### Bug Fixes

* **docs:** fixed javadoc comments ([b6aa6bd](https://github.com/hexonet/java-sdk/commit/b6aa6bdc407aeadf617a4a0ac2921e7c9062a1c9))
* **messaging:** return a specific error template in case of missing API response code or description ([500934b](https://github.com/hexonet/java-sdk/commit/500934b8925fd90261f8f8eba465cb4be0ee3f85))
* **security:** replace passwords whereever they could be used for output ([65860ab](https://github.com/hexonet/java-sdk/commit/65860abdfe8d4bb07d5500f952403d864df9b443))


### Features

* **apiclient:** allow to specify additional libraries via setUserAgent ([58c7eb2](https://github.com/hexonet/java-sdk/commit/58c7eb244e548f224a01df69ebfea4dff3e9df05))
* **apiclient:** automatic IDN conversion of API command parameters to punycode ([afc36af](https://github.com/hexonet/java-sdk/commit/afc36af743e872025ab3242d45fdc7c0fe6a8bd6))
* **apiclient:** support the `High Performance Proxy Setup`. see README.md ([446ec48](https://github.com/hexonet/java-sdk/commit/446ec4893006e40f47c3aee98ab52def66bd182d))
* **response:** added getCommandPlain (getting used command in plain text) ([9dfe033](https://github.com/hexonet/java-sdk/commit/9dfe03363e279f61694f56092d24174817d5e020))
* **response:** possibility of placeholder vars in standard responses to improve error details ([5590557](https://github.com/hexonet/java-sdk/commit/55905572304382175dd583735e0c4038ed842290))

# [3.0.0](https://github.com/hexonet/java-sdk/compare/v2.3.4...v3.0.0) (2020-03-12)


### Bug Fixes

* **travis:** move allow_failures from matrix to jobs ([2237296](https://github.com/hexonet/java-sdk/commit/22372968a6307951b62fbf5450e12feda060c746))


### Features

* **apiclient:** request method now accepts HashMap<String, Object> as command (allow nested arrays) ([35e500d](https://github.com/hexonet/java-sdk/commit/35e500d25919c9095c78b148c96fdc396bf47167))


### BREAKING CHANGES

* **apiclient:** changed type for parameter cmd of APIClient method request

## [2.3.4](https://github.com/hexonet/java-sdk/compare/v2.3.3...v2.3.4) (2019-10-04)


### Bug Fixes

* **responsetemplate/mgr:** improve description for `423 Empty API response` ([09d6060](https://github.com/hexonet/java-sdk/commit/09d6060))

## [2.3.3](https://github.com/hexonet/java-sdk/compare/v2.3.2...v2.3.3) (2019-09-19)


### Bug Fixes

* **release process:** check if plugin order matters ([8618655](https://github.com/hexonet/java-sdk/commit/8618655))

## [2.3.2](https://github.com/hexonet/java-sdk/compare/v2.3.1...v2.3.2) (2019-09-19)


### Bug Fixes

* **release process:** add `-ntp` parameter to mvn calls ([e2b992e](https://github.com/hexonet/java-sdk/commit/e2b992e))
* **release process:** change logger level for Downloads to `warn` ([efa5284](https://github.com/hexonet/java-sdk/commit/efa5284))
* **release process:** fix maven 3.6.2 download url ([d3889d9](https://github.com/hexonet/java-sdk/commit/d3889d9))
* **release process:** fix maven command line calls ([94ebb3a](https://github.com/hexonet/java-sdk/commit/94ebb3a))
* **release process:** fix maven version number ([65b27ab](https://github.com/hexonet/java-sdk/commit/65b27ab))
* **release process:** fix maven version number ([688fbfc](https://github.com/hexonet/java-sdk/commit/688fbfc))
* **release process:** migrate configuration ([3fa221e](https://github.com/hexonet/java-sdk/commit/3fa221e))
* **release process:** try maven_opts review ([9f15670](https://github.com/hexonet/java-sdk/commit/9f15670))
* **release process:** try maven_opts review ([56a5b71](https://github.com/hexonet/java-sdk/commit/56a5b71))
* **release process:** upgrade maven to 3.6.2 ([97740e8](https://github.com/hexonet/java-sdk/commit/97740e8))
* **release process:** use -B instead of -ntp for maven ([12807df](https://github.com/hexonet/java-sdk/commit/12807df))
* **release process:** verify maven version number and paths ([5054df2](https://github.com/hexonet/java-sdk/commit/5054df2))

## [2.3.1](https://github.com/hexonet/java-sdk/compare/v2.3.0...v2.3.1) (2019-08-19)


### Bug Fixes

* **APIClient:** change default SDK url ([969d01c](https://github.com/hexonet/java-sdk/commit/969d01c))

# [2.3.0](https://github.com/hexonet/java-sdk/compare/v2.2.1...v2.3.0) (2019-04-17)


### Features

* **responsetemplate:** add isPending method ([28657fa](https://github.com/hexonet/java-sdk/commit/28657fa))

## [2.2.1](https://github.com/hexonet/java-sdk/compare/v2.2.0...v2.2.1) (2019-04-04)


### Bug Fixes

* **APIClient:** return APIClient instance in setUserAgent method ([42447b4](https://github.com/hexonet/java-sdk/commit/42447b4))

# [2.2.0](https://github.com/hexonet/java-sdk/compare/v2.1.4...v2.2.0) (2019-04-02)


### Features

* **apiclient:** review user agent usage ([e2c276d](https://github.com/hexonet/java-sdk/commit/e2c276d))

## [2.1.4](https://github.com/hexonet/java-sdk/compare/v2.1.3...v2.1.4) (2018-11-30)


### Bug Fixes

* **releasing:** add missing maven-site-plugin ([375345b](https://github.com/hexonet/java-sdk/commit/375345b))
* **Response:** make Response class public ([87a9cfa](https://github.com/hexonet/java-sdk/commit/87a9cfa))

## [2.1.3](https://github.com/hexonet/java-sdk/compare/v2.1.2...v2.1.3) (2018-11-30)


### Bug Fixes

* **release:** review pom.xml to include javadoc etc. in package phase ([54a3ce9](https://github.com/hexonet/java-sdk/commit/54a3ce9))

## [2.1.2](https://github.com/hexonet/java-sdk/compare/v2.1.1...v2.1.2) (2018-11-29)


### Bug Fixes

* **pom.xml:** review config + test packaging/releasing ([3cbfed9](https://github.com/hexonet/java-sdk/commit/3cbfed9))

## [2.1.1](https://github.com/hexonet/java-sdk/compare/v2.1.0...v2.1.1) (2018-11-29)


### Bug Fixes

* **mvn:** review deployment ([bc21eed](https://github.com/hexonet/java-sdk/commit/bc21eed))

# [2.1.0](https://github.com/hexonet/java-sdk/compare/v2.0.2...v2.1.0) (2018-11-29)


### Bug Fixes

* **pom.xml:** test releasing ([c5497a7](https://github.com/hexonet/java-sdk/commit/c5497a7))
* **travis:** fix versioning issue ([e432694](https://github.com/hexonet/java-sdk/commit/e432694))
* **travis:** review travis/semantic-release config ([216b3c8](https://github.com/hexonet/java-sdk/commit/216b3c8))


### Features

* **travis:** cleanup before_script ([8d7f207](https://github.com/hexonet/java-sdk/commit/8d7f207))

# [2.0.2](https://github.com/hexonet/java-sdk/compare/v2.0.1...v2.0.2) (2018-11-29)


### Bug Fixes

* **travis:** fix semantic-release call ([cc6c1bf](https://github.com/hexonet/java-sdk/commit/cc6c1bf))

# [2.0.1](https://github.com/hexonet/java-sdk/compare/v2.0.0...v2.0.1) (2018-11-29)


### Bug Fixes

* **travis:** rename maven settings file ([71a9722](https://github.com/hexonet/java-sdk/commit/71a9722))

# [2.0.0](https://github.com/hexonet/java-sdk/compare/v1.3.19...v2.0.0) (2018-11-29)

### Code Refactoring

* **pkg:** review into direction of cross-SDK UML diagram ([0f30646)](https://github.com/hexonet/java-sdk/commit/0f30646))

### BREAKING CHANGES

* **pkg:** downward incompatible. check the documentation on how to migrate.

### Changelog

All notable changes to this project will be documented in this file. Dates are displayed in UTC.

#### [v1.3.20](https://github.com/hexonet/java-sdk/compare/v1.3.19...v1.3.20) (22 August 2018)

- [maven-release-plugin] Update HISTORY.md [`f2ac337`](https://github.com/hexonet/java-sdk/commit/f2ac337d7b7850c1ab8d37dd4fbf30c2772760d7)
- [maven-release-plugin] Update HISTORY.md [`d5a687c`](https://github.com/hexonet/java-sdk/commit/d5a687c49d5df4cfbfbabac24af273465c162fbe)
- [maven-release-plugin] prepare for next development iteration [`572d7bc`](https://github.com/hexonet/java-sdk/commit/572d7bcead850e910ecb24ec6f6d7deb97022572)

#### [v1.3.19](https://github.com/hexonet/java-sdk/compare/v1.3.18...v1.3.19) (22 August 2018)

- Update README.md [`aba2bf4`](https://github.com/hexonet/java-sdk/commit/aba2bf4bd07a339c48f3dc8895a55ae858bc0dac)
- update dependency configuration [`82805b6`](https://github.com/hexonet/java-sdk/commit/82805b60546fc156fe021e46fc49ac98cde43c03)
- HM-347 fix ArrayIndexOutOfBoundsException [`b52ea1a`](https://github.com/hexonet/java-sdk/commit/b52ea1a61aab15d8cdb499ea3de6477e6f991e70)
- [maven-release-plugin] Update HISTORY.md [`d5446a4`](https://github.com/hexonet/java-sdk/commit/d5446a4d8a46bcf07560e5e90d7acc0be4aa2172)
- Update README.md [`171eadc`](https://github.com/hexonet/java-sdk/commit/171eadca7f2f4e1f0307345d3664fadc8e56e9d8)
- play with plugin version because of git issue [`76849a3`](https://github.com/hexonet/java-sdk/commit/76849a3e996181164e7fb051601ecd69c6ccde91)
- updateded plugin section [`f1a8c32`](https://github.com/hexonet/java-sdk/commit/f1a8c3239fb2db854c621f8e4e27512685e4ad4b)
- readme: add quick navigation [`3f4e58c`](https://github.com/hexonet/java-sdk/commit/3f4e58c0d0e8d15503971691f5fce9c3962175c7)
- Update CONTRIBUTING.md [`238170f`](https://github.com/hexonet/java-sdk/commit/238170f590852a563845ce964e69c7b5de75482b)
- Update README.md [`86ac8bf`](https://github.com/hexonet/java-sdk/commit/86ac8bfea7b523c7500c29b6117444197b374550)
- Update README.md [`34862c1`](https://github.com/hexonet/java-sdk/commit/34862c1c09ac201acc95ef90c4b5f856f8fcfb29)
- changelog: use custom template [`2192a63`](https://github.com/hexonet/java-sdk/commit/2192a63199a7f828bda0687cee2ba70c7db12d4e)
- [maven-release-plugin] prepare for next development iteration [`2336d97`](https://github.com/hexonet/java-sdk/commit/2336d97e680d2fda0ac126e40210a2ab6591c428)
- dep bump [`720f931`](https://github.com/hexonet/java-sdk/commit/720f9310f1ea08289e8d880e2d7323f336a9fee4)
- readme: minor review of quick navigation [`17cb9e4`](https://github.com/hexonet/java-sdk/commit/17cb9e4fe4369fa96e1dec546d43154ccc578b26)
- updated release version [`ad0af2f`](https://github.com/hexonet/java-sdk/commit/ad0af2fbf421b401bd196d531bc8dd1e6e98ffa8)
- updated scm section [`bd266e6`](https://github.com/hexonet/java-sdk/commit/bd266e65f354408281fe7b94f518aa654f650bba)
- Update README.md [`3fb6139`](https://github.com/hexonet/java-sdk/commit/3fb61398f2e36f6921bc7a0e1d1f1eeb429e7fca)
- updated changelog format [`3a6f254`](https://github.com/hexonet/java-sdk/commit/3a6f254e23ecbd628a8000df095080a1f8829d2d)
- [maven-release-plugin] Update HISTORY.md [`8a46946`](https://github.com/hexonet/java-sdk/commit/8a46946d525201355f13747c42d7b5c07cdaa8ef)
- readme: update quicklink to SDK Documentation [`be406f2`](https://github.com/hexonet/java-sdk/commit/be406f257072ce8f698a8604524f8658f1d6dace)
- Update CONTRIBUTING.md [`e942340`](https://github.com/hexonet/java-sdk/commit/e9423408d1f2ef30b98e43a342590602990964b7)
- Update README.md [`40b499b`](https://github.com/hexonet/java-sdk/commit/40b499b603135074e1fbab0f4fc26c4576d56dae)
- readme: shortened backlink to hexonet api [`b73f449`](https://github.com/hexonet/java-sdk/commit/b73f44971bc50f95aa6519138484b5146c6579f9)
- updated scm url to https [`24ace33`](https://github.com/hexonet/java-sdk/commit/24ace339e23b61fb1d1c02d92f11098cf8d982e0)
- readme: update quicklink to release notes [`b25aeeb`](https://github.com/hexonet/java-sdk/commit/b25aeebbcdbc908c93e2deaaf3b36fa8e62a93b6)
- Update README.md [`cf7d216`](https://github.com/hexonet/java-sdk/commit/cf7d216ac833355493f2d65d5ecb87f65d662d0a)
- use 2.5.2 auf maven-release-plugin -> test [`2e8bd68`](https://github.com/hexonet/java-sdk/commit/2e8bd686dd47e78862fb78b5a9d365003574d496)
- 1.3.19-SNAPSHOT release [`e632685`](https://github.com/hexonet/java-sdk/commit/e632685414ef70fbb676cce51d98f27c1ef2c36e)
- HM-329 changed changelog template [`43f5be9`](https://github.com/hexonet/java-sdk/commit/43f5be978fbd970bb3d8b87fcbca6eb4bd2d597d)
- readded tag to scm [`4a19614`](https://github.com/hexonet/java-sdk/commit/4a196145d1bd70d30fd65807ae8cae8331584516)
- readme: add backlink to the hexonet backend api [`6e4ec28`](https://github.com/hexonet/java-sdk/commit/6e4ec28ca98c6f6bb7e1d99534ecc926848338cf)

#### [v1.3.18](https://github.com/hexonet/java-sdk/compare/v1.3.17...v1.3.18) (2 July 2018)

- [maven-release-plugin] Update HISTORY.md [`51443c8`](https://github.com/hexonet/java-sdk/commit/51443c83d77a671470669a2c76197dc3853b5420)
- added nodejs changelog generator [`a74a0f7`](https://github.com/hexonet/java-sdk/commit/a74a0f7cf8090023329dde832697d1576572c96a)
- [maven-release-plugin] prepare release v1.3.18 [`03331f9`](https://github.com/hexonet/java-sdk/commit/03331f99a84b3b97a1d46321a7bd94fb7a33b57d)
- [maven-release-plugin] prepare for next development iteration [`fc2ca2e`](https://github.com/hexonet/java-sdk/commit/fc2ca2e16a862666d3fae6ca40a6ac2d19399f6c)
- [maven-release-plugin] Update HISTORY.md [`430b27e`](https://github.com/hexonet/java-sdk/commit/430b27ec443cfdc2aae740c136302ea33f34f165)

#### [v1.3.17](https://github.com/hexonet/java-sdk/compare/v1.3.16...v1.3.17) (2 July 2018)

- updated changelog plugin configuration [`3d9ec31`](https://github.com/hexonet/java-sdk/commit/3d9ec31774ad43c672de8eb1934b43abf00548ea)
- updated site & apidocs folder [`ba67d79`](https://github.com/hexonet/java-sdk/commit/ba67d79892f744cde906b52f0ff5ee0c93c38027)
- Update README.md [`2668ed5`](https://github.com/hexonet/java-sdk/commit/2668ed53cbd686887c69560805fe68ee105e75d4)
- upgrade maven-changelog-plugin to 0.3.0 [`6ee3bea`](https://github.com/hexonet/java-sdk/commit/6ee3beab7b10a567982194af3106c23c91049692)
- [maven-release-plugin] Update HISTORY.md [`3645570`](https://github.com/hexonet/java-sdk/commit/36455702ffbf3d39eb0e1170b4190eccde2e0a5e)
- [maven-release-plugin] prepare for next development iteration [`87eb2d0`](https://github.com/hexonet/java-sdk/commit/87eb2d0cc1a76be5e30ead33ca57566ca396c832)
- [maven-release-plugin] prepare release v1.3.17 [`5393476`](https://github.com/hexonet/java-sdk/commit/539347644afdcdcc3e95073f9a0a19bde1ef5c64)
- Update README.md [`870031a`](https://github.com/hexonet/java-sdk/commit/870031a76538d3901a7f0506d548054ac66c6d24)
- readme: udpate license badge [`fd9cd05`](https://github.com/hexonet/java-sdk/commit/fd9cd05fa6e782eb7327855171835af48e821656)
- readme: update contribute badge [`e7a6c1d`](https://github.com/hexonet/java-sdk/commit/e7a6c1d3c9e718d1a393117071dab1eed7823c87)
- [maven-release-plugin] Update CHANGELOG.md [`54980fe`](https://github.com/hexonet/java-sdk/commit/54980fef6695e4d712638605af0105f7a23ad8ce)
- readme: add slack badge [`7f9d8c6`](https://github.com/hexonet/java-sdk/commit/7f9d8c6dff4f4a06aa7f481d0f9f7bf936c907ea)

#### [v1.3.16](https://github.com/hexonet/java-sdk/compare/v1.3.15...v1.3.16) (27 June 2018)

- test removal maven-deploy-plugin in favor of nexus-staging-maven-plugin [`88ddad0`](https://github.com/hexonet/java-sdk/commit/88ddad0160b141a003d000a0468e8fc7fb8ad893)
- [maven-release-plugin] Update CHANGELOG.md [`370b95c`](https://github.com/hexonet/java-sdk/commit/370b95c4d9ebe118244a6ae853cb8d7d9aedf87b)
- [maven-release-plugin] prepare release v1.3.16 [`7abbf62`](https://github.com/hexonet/java-sdk/commit/7abbf62069880d0755635cee712add27b3cff6dd)
- [maven-release-plugin] prepare for next development iteration [`4e36bc7`](https://github.com/hexonet/java-sdk/commit/4e36bc751fc3dd7ad0391ee56aeeb1de7470ee73)
- Update README.md [`c3fa9e6`](https://github.com/hexonet/java-sdk/commit/c3fa9e68a7e4573d4f0962b4adebe9c300b9d51c)
- Update README.md [`ea60a4f`](https://github.com/hexonet/java-sdk/commit/ea60a4f4ddb556eef16966674184eef3e617cf2d)
- Update README.md [`278fb3b`](https://github.com/hexonet/java-sdk/commit/278fb3b932ee51285af9ee78ab3237336b0ba344)
- [maven-release-plugin] Update CHANGELOG.md [`524b902`](https://github.com/hexonet/java-sdk/commit/524b9022ba3aa5847525bbe009d8f72fca5537b3)

#### [v1.3.15](https://github.com/hexonet/java-sdk/compare/v1.3.14...v1.3.15) (26 June 2018)

- updated docs [`e2cfc45`](https://github.com/hexonet/java-sdk/commit/e2cfc45d51223ebfe2723cb876d43531354ab694)
- updated site/apidocs [`599129a`](https://github.com/hexonet/java-sdk/commit/599129a86506ef745595ab15e6d68bdc699e395f)
- [maven-release-plugin] Update CHANGELOG.md [`4ebf2d7`](https://github.com/hexonet/java-sdk/commit/4ebf2d7cfb62d5f40d23f2a32cb00da9bfabb44e)
- Update README.md [`b885d4e`](https://github.com/hexonet/java-sdk/commit/b885d4e7c84064dea80f0b4d500530e600b867f1)
- [maven-release-plugin] prepare release v1.3.15 [`1128bee`](https://github.com/hexonet/java-sdk/commit/1128beecdb0d9f4f6defc530f62aaedf1f175f8b)
- lint readme [`8a40ea0`](https://github.com/hexonet/java-sdk/commit/8a40ea0fc19cd68424d918d797c347a6203eaf31)
- maven-deploy-plugin: updateReleaseInfo -> true [`228aa01`](https://github.com/hexonet/java-sdk/commit/228aa01a58d18fec70f245374231ecef151344ba)
- Update README.md [`7520bb6`](https://github.com/hexonet/java-sdk/commit/7520bb6528f8d39032611e779401c48ca755c6b8)
- [maven-release-plugin] prepare for next development iteration [`4e054f8`](https://github.com/hexonet/java-sdk/commit/4e054f8c7720990201206b4f86c753cd184d8d7a)
- Update README.md [`7c8b1e7`](https://github.com/hexonet/java-sdk/commit/7c8b1e72e795f169278e934d4d9a24e49b815c7a)
- Update README.md [`27a83b4`](https://github.com/hexonet/java-sdk/commit/27a83b4d8a3547c01ae1fcb73c35b914abeb7a4d)
- Update README.md [`cee1b99`](https://github.com/hexonet/java-sdk/commit/cee1b9967296cbd910902a974b7c6ab4c039f9ab)
- Update README.md [`3856701`](https://github.com/hexonet/java-sdk/commit/3856701ac7851f7a61853d595a90adad3575311a)
- [maven-release-plugin] Update CHANGELOG.md [`ffe8fc4`](https://github.com/hexonet/java-sdk/commit/ffe8fc4f402211c93c1b6ff45a084b225dea1680)
- Update README.md [`8445deb`](https://github.com/hexonet/java-sdk/commit/8445debe439d2323cfdd7c1420b7682705ff7834)
- Update README.md [`2cb7263`](https://github.com/hexonet/java-sdk/commit/2cb7263f9b1f66e1077f85c207792b642caa9d8c)
- Update README.md [`df25f7c`](https://github.com/hexonet/java-sdk/commit/df25f7cd5c54d4d0c25f1d664aceaf06c3fc36ed)
- Update README.md [`1d358be`](https://github.com/hexonet/java-sdk/commit/1d358beadb59fba5804abf684b6b7bf52fe9c579)
- Update README.md [`93f6531`](https://github.com/hexonet/java-sdk/commit/93f6531497e29de56f3f5ccfe8e2bed80082aa5c)

#### [v1.3.14](https://github.com/hexonet/java-sdk/compare/v1.3.13...v1.3.14) (26 June 2018)

- updated site and api docs [`98d2112`](https://github.com/hexonet/java-sdk/commit/98d211287ee1c9c68cc3e63c6b95e358aeb3c236)
- OSSRH-40778 try to fix by updated pom.xml [`9b26eb9`](https://github.com/hexonet/java-sdk/commit/9b26eb9e01e376ce438c61029fbc93385e8a5def)
- OSSRH-40778 fix malformed pom.xml [`98cf091`](https://github.com/hexonet/java-sdk/commit/98cf091a62e3efa25d7fcbe3a5210aee06c553cb)
- [maven-release-plugin] Update CHANGELOG.md [`b32f818`](https://github.com/hexonet/java-sdk/commit/b32f818ca4089e3c37682bee0a5c9f672588bee8)
- [maven-release-plugin] prepare release v1.3.14 [`08134ae`](https://github.com/hexonet/java-sdk/commit/08134ae3b40e14a3baa36375b63d8ca7503edee8)
- [maven-release-plugin] prepare for next development iteration [`1662581`](https://github.com/hexonet/java-sdk/commit/1662581569b05203bb345eabbf354ee31adc02cf)
- fix readme [`19e4e53`](https://github.com/hexonet/java-sdk/commit/19e4e537aa710a1bd30db1532443097d561f7238)
- [maven-release-plugin] Update CHANGELOG.md [`f024541`](https://github.com/hexonet/java-sdk/commit/f024541213e660995270e907c0f282c01c291a64)

#### [v1.3.13](https://github.com/hexonet/java-sdk/compare/v1.3.12...v1.3.13) (25 June 2018)

- added apidocs [`43cdae3`](https://github.com/hexonet/java-sdk/commit/43cdae3fe0bfbef66c8690c4c96deada1d8ca723)
- updated readme [`de973ce`](https://github.com/hexonet/java-sdk/commit/de973cee32fc08c05af19a2ddfc8d1f5cec434be)
- Update README.md [`8426948`](https://github.com/hexonet/java-sdk/commit/8426948d0372354c2358f9afe732d9a935404700)
- added maven-central OSSRH integration [`efdf5eb`](https://github.com/hexonet/java-sdk/commit/efdf5ebeb1adf700de85bb46bebde8e499920276)
- Update README.md [`ed95e01`](https://github.com/hexonet/java-sdk/commit/ed95e01d9ed5bf531a7b7388d2eb06c3717e290e)
- Update README.md [`955e347`](https://github.com/hexonet/java-sdk/commit/955e34758c6090c96a44190dffee684943e7f30c)
- updated readme [`552c1e0`](https://github.com/hexonet/java-sdk/commit/552c1e0849c0ae16a286212fdf23221463ca5f6d)
- preparations for maven central repository [`5482390`](https://github.com/hexonet/java-sdk/commit/54823905f09857f61a0c9c24d9057bbacd7f3ad7)
- [maven-release-plugin] Update CHANGELOG.md [`72079e1`](https://github.com/hexonet/java-sdk/commit/72079e1351d19dca5195b7f8f4c6eb366353bfe2)
- Update CONTRIBUTING.md [`8f2d0dd`](https://github.com/hexonet/java-sdk/commit/8f2d0dd1f563d575cc16842f5be1f61cd2dae308)
- Update README.md [`58f0675`](https://github.com/hexonet/java-sdk/commit/58f0675f379ff9e3655152881f2a2ed4b350bff8)
- [maven-release-plugin] prepare for next development iteration [`fa4f619`](https://github.com/hexonet/java-sdk/commit/fa4f619b268d7b0742a40f5d334cfaa61e1b2ba2)
- [maven-release-plugin] prepare release v1.3.13 [`9a8d0dd`](https://github.com/hexonet/java-sdk/commit/9a8d0dde13577a2114a76d9e28e15512fcc312ac)
- Update README.md [`51abe79`](https://github.com/hexonet/java-sdk/commit/51abe791cd983846ffd6a7ae3a899f604a112538)
- [maven-release-plugin] Update CHANGELOG.md [`b0ae12a`](https://github.com/hexonet/java-sdk/commit/b0ae12ac7bec42e179b41f48f317a51e63499bd6)
- Update README.md [`0754762`](https://github.com/hexonet/java-sdk/commit/075476233654852d3deb88f4694f1ba0c675933a)

#### v1.3.12 (18 June 2018)

- [maven-release-plugin] prepare release v1.3.12 [`b676a56`](https://github.com/hexonet/java-sdk/commit/b676a56995061c15bab74e02a2f6eb6095f42903)
- [maven-release-plugin] Update CHANGELOG.md [`83dc492`](https://github.com/hexonet/java-sdk/commit/83dc49219a238b7a6741439562e8e9c97e600909)
- initial release update [`85efb14`](https://github.com/hexonet/java-sdk/commit/85efb14cca5ef88c8330345cd57a61db1d9c6279)
- initial public release [`a1d5609`](https://github.com/hexonet/java-sdk/commit/a1d560969ab2ba4284fe3ea1b8038b2aa36b9519)
- Initial commit [`76ea3e1`](https://github.com/hexonet/java-sdk/commit/76ea3e1605515afd59c6a33c9276a7eabcd6a597)
