/* jshint jasmine: true */

exports.defineAutoTests = function () {
  describe('onegini', function () {
    it("onegini should exist", function () {
      expect(window.onegini).toBeDefined();
    });

    it("onegini.start should exist", function () {
      expect(onegini.start).toBeDefined();
    });
  });

  describe('onegini.user', function () {
    it("onegini.user should exist", function () {
      expect(onegini.user).toBeDefined();
    });

    it("onegini.user.startRegistration should exist", function () {
      expect(onegini.user.startRegistration).toBeDefined();
    });

    it("onegini.user.createPin should exist", function () {
      expect(onegini.user.createPin).toBeDefined();
    });

    it("onegini.user.getUserProfiles should exist", function () {
      expect(onegini.user.getUserProfiles).toBeDefined();
    });

    it("onegini.user.startAuthentication should exist", function () {
      expect(onegini.user.startAuthentication).toBeDefined();
    });

    it("onegini.user.checkPin should exist", function () {
      expect(onegini.user.checkPin).toBeDefined();
    });
  });
};