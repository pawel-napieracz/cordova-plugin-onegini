/* jshint jasmine: true */

exports.defineAutoTests = function () {
  describe('onegini', function () {
    it("onegini should exist", function () {
      expect(window.onegini).toBeDefined();
    });

    describe('onegini.start', function () {
      it("onegini.start should exist", function () {
        expect(onegini.start).toBeDefined();
      });
    });
  });

  describe('onegini.user', function () {
    it("onegini.user should exist", function () {
      expect(onegini.user).toBeDefined();
    });

    describe('onegini.user.startRegistration', function () {
      it("onegini.user.startRegistration should exist", function () {
        expect(onegini.user.startRegistration).toBeDefined();
      });
    });

    describe('onegini.user.createPin', function () {
      it("onegini.user.createPin should exist", function () {
        expect(onegini.user.createPin).toBeDefined();
      });

      it("onegini.user.createPin success cb mandatory", function () {
        expect(function () {
          onegini.user.createPin([]);
        }).toThrow(new TypeError("Onegini: missing success callback for createPin"));
      });

      it("onegini.user.createPin 'pin' argument mandatory", function () {
        expect(function () {
          onegini.user.createPin([], function(){}, function(){});
        }).toThrow(new TypeError("Onegini: missing 'pin' argument for createPin"));
      });

      /* This is an example of checking error callbacks, which we'll prolly need in the future
      it("onegini.user.createPin empty pin invokes error cb", function (done) {
        onegini.user.createPin(
            [],
            function (msg) {
              expect(msg).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("pin is mandatory");
              done();
            });
      });
      */
    });

    describe('onegini.user.getUserProfiles', function () {
      it("onegini.user.getUserProfiles should exist", function () {
        expect(onegini.user.getUserProfiles).toBeDefined();
      });
    });

    describe('onegini.user.startAuthentication', function () {
      it("onegini.user.startAuthentication should exist", function () {
        expect(onegini.user.startAuthentication).toBeDefined();
      });
    });

    describe('onegini.user.checkPin', function () {
      it("onegini.user.checkPin should exist", function () {
        expect(onegini.user.checkPin).toBeDefined();
      });
    });

    describe('onegini.user.deregister', function () {
      it("onegini.user.deregister should exist", function () {
        expect(onegini.user.deregister).toBeDefined();
      });

      it("onegini.user.deregister 'profileId' argument mandatory", function () {
        expect(function () {
          onegini.user.deregister({}, function () {
          }, function () {
          });
        }).toThrow(new TypeError("Onegini: missing 'profileId' argument for deregister"));
      });

      it("onegini.user.deregister no user found for profileId", function (done) {
        onegini.user.deregister(
            {
              profileId: "UNKNOWN"
            },
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("Onegini: No registered user found for the provided profileId.");
              done();
            });
      });

      it("onegini.user.deregister should succees with correct profileId", function (done) {
        onegini.user.deregister(
            {
              profileId: "TODO" // TODO use internally cached profileId
            },
            function (result) {
              expect(result).toBeDefined();
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

  });
};