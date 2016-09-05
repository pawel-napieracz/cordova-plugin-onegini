/* jshint jasmine: true */

exports.defineAutoTests = function () {
  describe('onegini', function () {
    it("onegini should exist", function () {
      expect(window.onegini).toBeDefined();
    });

    describe('start', function () {
      it("should exist", function () {
        expect(onegini.start).toBeDefined();
      });

      it("should run ok", function (done) {
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
    it("should exist", function () {
      expect(onegini.user).toBeDefined();
    });

    describe("register", function () {
      it("should have a start method", function () {
        expect(onegini.user.register.start).toBeDefined();
      });

      it("should have a createPin method", function () {
        expect(onegini.user.register.createPin).toBeDefined();
      });

      describe("createPin", function () {
        it("should require a pincode argument", function () {
          expect(function () {
            onegini.user.register.createPin({}, function () {
            }, function () {
            });
          }).toThrow(new TypeError("Onegini: missing 'pin' argument for createPin"));
        });

        it("should require a success callback", function () {
          expect(function () {
            onegini.user.register.createPin({
              pin: "12356"
            })
          }).toThrow(new TypeError("Onegini: missing argument for method. 'createPin' requires a Success Callback"));
        });

        it("can't be called before 'start' method", function (done) {
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
      });

      describe('start', function () {
        it("should return pinlength of '5'", function (done) {
          onegini.user.register.start(
              undefined,
              function (result) {
                expect(result).toBeDefined();
                expect(result.pinLength).toBe(5);
                done();
              },
              function (err) {
                expect(err).toBeUndefined();
              });
        });

      });

      describe('createPin', function () {
        it("should return a profileId", function (done) {
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