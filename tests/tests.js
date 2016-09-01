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

      it("onegini.start should run ok", function (done) {
        onegini.start(function () {
              expect(true).toBe(true);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });
  });

  describe('onegini.user', function () {
    it("onegini.user should exist", function () {
      expect(onegini.user).toBeDefined();
    });

    describe('onegini.user.register', function () {
      it("onegini.user.register.start should exist", function () {
        expect(onegini.user.register.start).toBeDefined();
      });

      it("onegini.user.register.createPin should exist", function () {
        expect(onegini.user.register.createPin).toBeDefined();
      });

      it("onegini.user.register.createPin 'pin' argument mandatory", function () {
        expect(function () {
          onegini.user.register.createPin({}, function () {
          }, function () {
          });
        }).toThrow(new TypeError("Onegini: missing 'pin' argument for createPin"));
      });

      it("onegini.user.register.createPin success cb mandatory", function () {
        expect(function () {
          onegini.user.register.createPin(
              {
                pin: '12346'
              });
        }).toThrow(new TypeError("Onegini: missing argument for method. 'createPin' requires a Success Callback"));
      });

      it("onegini.user.register.createPin can't be called before 'start' is called", function (done) {
        onegini.user.register.createPin(
            {
              pin: '12346'
            },
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("Onegini: createPin called, but no registration in process. Did you call 'onegini.user.register.start'?");
              done();
            });
      });

      it("onegini.user.register.start should return pinLength 5", function (done) {
        onegini.user.register.start(
            undefined, // scopes are optional, so checking that as well
            function (result) {
              expect(result).toBeDefined();
              expect(result.pinLength).toBe(5);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });

      it("onegini.user.register.createPin should return a profileId", function (done) {
        onegini.user.register.createPin(
            {
              pin: '12346'
            },
            function (result) {
              expect(result).toBeDefined();
              expect(result.profileId).toBeDefined();
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

    describe('onegini.user.getUserProfiles', function () {
      it("onegini.user.getUserProfiles should exist", function () {
        expect(onegini.user.getUserProfiles).toBeDefined();
      });
    });

    describe('onegini.user.authenticate.start', function () {
      it("onegini.user.authenticate.start should exist", function () {
        expect(onegini.user.authenticate.start).toBeDefined();
      });
    });

    describe('onegini.user.authenticate.providePin', function () {
      it("onegini.user.authenticate.providePin should exist", function () {
        expect(onegini.user.authenticate.providePin).toBeDefined();
      });
    });
  });
};