/* jshint jasmine: true */

exports.defineAutoTests = function () {
  describe('onegini.start', function () {
    it("should exist", function () {
      expect(onegini.start).toBeDefined();
    });
  });
};