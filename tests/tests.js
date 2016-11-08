/* jshint jasmine: true */


exports.defineAutoTests = function () {
  var config = {
    testForMultipleAuthenticators: true,
    testForMobileFingerprintAuthentication: false,
    get platform() {
      return navigator.userAgent.indexOf("Android") > -1 ? "android" : "ios"
    },
    get fingerPrintAuthenticatorID() {
      return navigator.userAgent.indexOf("Android") > -1 ? "com.onegini.authenticator.Fingerprint" : "com.onegini.authenticator.TouchID"
    }
  };

  var registeredProfileId,
      nrOfUserProfiles,
      pin = "12356";

  if (!config.testForMultipleAuthenticators) {
    console.warn("Testing for multiple authenticators disabled");
  }

  if (!config.testForMobileFingerprintAuthentication) {
    console.warn("Testing for mobile fingerprint authentication disabled (requires interaction)");
  }

  function sendMobileAuthenticationRequest(type) {
    var xhr = new XMLHttpRequest();

    type = type || "push";

    xhr.open("POST", "https://demo-msp.onegini.com/oauth/api/v2/authenticate/user");
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Authorization", "Basic ODgyMzRCMEU5MzIzNzFCNzY3N0I2QkZCNUFGQTJGMTI1QjY3NkNGNTNBMTExREFGRjQyNjQ3NzM5QzRGMDVDNTo1MTE2NzA5OTM4QUE1MkY2RkI5NDkwRDc3MUE1QzQ0Rjk4N0QxRUE3ODJERUMwNEQwRTM4NzA5NEJBMzVGMzM5");

    xhr.onreadystatechange = function () {
      if (this.readyState === 4) {
        if (this.status !== 200) {
          console.error("Failed to send mobile authentication request!", JSON.parse(this.responseText));
        }
      }
    };

    xhr.send("callback_uri=https://wwww.onegini.com&message=Test&type=" + type + "&user_id=testclientuserid");
  }

  /******** onegini *********/

  describe('onegini', function () {
    it("should exist", function () {
      expect(window.onegini).toBeDefined();
    });

    describe('start', function () {
      it("should exist", function () {
        expect(onegini.start).toBeDefined();
      });

      it("should run ok", function (done) {
        onegini.start(
            {
              secureXhr: true
            },
            function () {
              expect(true).toBe(true);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });
  });


  /******** onegini.user (1/2) *********/

  describe('onegini.user', function () {
    it("should exist", function () {
      expect(onegini.user).toBeDefined();
    });

    describe("validatePinWithPolicy", function () {
      it("should exist", function () {
        expect(onegini.user.validatePinWithPolicy).toBeDefined();
      });

      it("should fail because of an incorrect length", function (done) {
        onegini.user.validatePinWithPolicy(
            {
              pin: "incorrect"
            },
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.code).toBe(9014);
              done();
            });
      });


      it("should fail because of repeating numbers", function (done) {
        onegini.user.validatePinWithPolicy(
            {
              pin: "11111"
            },
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.code).toBe(9013);
              done();
            });
      });

      it("should succeed if pin is compliant to policy", function (done) {
        onegini.user.validatePinWithPolicy(
            {
              pin: pin
            },
            function () {
              expect(true).toBe(true);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

    describe("getAuthenticatedUserProfile (1/3)", function () {
      it("should exist", function () {
        expect(onegini.user.getAuthenticatedUserProfile).toBeDefined();
      });

      it("should fail", function (done) {
        onegini.user.getAuthenticatedUserProfile(
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("Onegini: No user authenticated.");
              done();
            });
      });
    });

    describe("register", function () {
      it("should exist", function () {
        expect(onegini.user.register).toBeDefined();
      });

      it("should succeed", function (done) {
        onegini.user.register()
            .onCreatePinRequest(function (actions, options) {
              expect(options.profileId).toBeDefined();
              expect(options.pinLength).toBe(5);
              registeredProfileId = options.profileId;
              actions.createPin(pin);
            })
            .onSuccess(function () {
              done();
            })
            .onError(function (err) {
              expect(err).toBeDefined();
              fail("Registration failed, but should have suceeded");
            });
      });
    });

    describe("getAuthenticatedUserProfile (2/3)", function () {
      it("should succeed", function (done) {
        onegini.user.getAuthenticatedUserProfile(
            function (result) {
              expect(result).toBeDefined();
              expect(result.profileId).toEqual(registeredProfileId);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

    describe('getUserProfiles (1/2)', function () {
      it("should exist", function () {
        expect(onegini.user.getUserProfiles).toBeDefined();
      });

      it("should not be empty", function (done) {
        onegini.user.getUserProfiles(
            function (result) {
              expect(result).toBeDefined();
              nrOfUserProfiles = result.length;
              expect(result[0]).toBeDefined();
              expect(result[0].profileId).toBeDefined();
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

    describe('logout', function () {
      it("should exist", function () {
        expect(onegini.user.logout).toBeDefined();
      });

      it("should succeed", function (done) {
        onegini.user.logout(
            function () {
              expect(true).toBe(true);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

    describe("authenticators (1/2)", function () {
      it("should have a getAll method", function () {
        expect(onegini.user.authenticators.getAll).toBeDefined();
      });

      it("should have a getRegistered method", function () {
        expect(onegini.user.authenticators.getRegistered).toBeDefined();
      });

      it("should have a getNotRegistered method", function () {
        expect(onegini.user.authenticators.getNotRegistered).toBeDefined();
      });

      it("should have a getPreferred method", function () {
        expect(onegini.user.authenticators.getPreferred).toBeDefined();
      })

      it("should have a setPreferred method", function () {
        expect(onegini.user.authenticators.setPreferred).toBeDefined();
      });

      it("should have a registerNew method", function () {
        expect(onegini.user.authenticators.registerNew).toBeDefined();
      });

      it("should have a deregister method", function () {
        expect(onegini.user.authenticators.deregister).toBeDefined();
      });

      describe("getRegistered", function () {
        it("should return an error when not logged in", function (done) {
          onegini.user.authenticators.getRegistered(
              function (result) {
                expect(result).toBeUndefined();
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              });
        });
      });

      describe('getNotRegistered', function () {
        it("should return an error when not logged in", function (done) {
          onegini.user.authenticators.getNotRegistered(
              function (result) {
                expect(result).toBeUndefined();
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              });
        });
      });

      describe("getPreferred", function () {
        it("should return an error when not logged in", function (done) {
          onegini.user.authenticators.getPreferred(
              function (result) {
                expect(result).toBeUndefined();
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              });
        });
      });

      describe("setPreferred", function () {
        it("should return an error when not logged in", function (done) {
          onegini.user.authenticators.setPreferred({
                authenticatorId: "com.onegini.authenticator.PIN"
              },
              function () {
                fail("Success callbacks was called, but method should have failed");
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              });
        });
      });

      describe("registerNew", function () {
        it("should exist", function () {
          expect(onegini.user.authenticators.registerNew).toBeDefined();
        });

        it("should require an authenticatorId argument", function () {
          expect(function () {
            onegini.user.authenticators.registerNew()
          }).toThrow(new TypeError("Onegini: missing 'authenticatorId' argument for authenticators.registerNew"));
        });

        it("should return an error when not logged in", function (done) {
          onegini.user.authenticators.registerNew('dummy')
              .onError(function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              })
        });
      });

      describe("deregister", function () {
        it("should require an authenticatorId", function () {
          expect(function () {
            onegini.user.authenticators.deregister()
          }).toThrow(new TypeError("Onegini: missing 'authenticatorId' argument for authenticators.deregister"));
        });

        it("should return an error when not logged in", function (done) {
          onegini.user.authenticators.deregister(
              {
                authenticatorId: 1
              },
              function () {
                fail("Success callback was called, but method should have failed");
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              });
        });
      });
    });

    describe("mobileAuthentication (1/3)", function () {
      describe('enroll', function () {
        it("should exist", function () {
          expect(onegini.mobileAuthentication.enroll).toBeDefined();
        });

        it("should return an error when not logged in", function (done) {
          onegini.mobileAuthentication.enroll(
              function (result) {
                expect(result).toBeUndefined();
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No user authenticated.");
                done();
              });
        });
      });
    });

    describe("authenticate", function () {
      it("should exist", function () {
        expect(onegini.user.authenticate).toBeDefined();
      });

      it("should require a profile ID", function () {
        expect(function () {
          onegini.user.authenticate();
        }).toThrow(new TypeError("Onegini: missing 'profileId' argument for user.authenticate"));
      });

      it("should succeed", function (done) {
        onegini.user.authenticate(registeredProfileId)
            .onPinRequest(function (actions, options) {
              expect(actions).toBeDefined();
              expect(actions.providePin).toBeDefined();
              expect(options).toBeDefined();

              if (options.remainingFailureCount = options.maxFailureCount - 1) {
                actions.providePin(pin);
              }
              else {
                console.log("remaining failure count", options.remainingFailureCount);
                console.log("max failure count", options.maxFailureCount);

                expect(options.remainingFailureCount).toBeDefined();
                expect(options.maxFailureCount).toBeDefined();
                expect(options.remainingFailureCount).toBe(3);
                expect(options.maxFailureCount).toBe(3);
                actions.providePin('incorrect');
              }
            })
            .onSuccess(function () {
              done();
            })
            .onError(function (err) {
              expect(err).toBeUndefined();
              fail("User authentication failed, but should have succeeded");
            });
      });

      it('should fail when user is already authenticated', function (done) {
        onegini.user.authenticate(registeredProfileId)
            .onSuccess(function () {
              fail("User authentication succeeded, but should have failed");
            })
            .onError(function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("Onegini: User already authenticated.");
              done();
            });
      });
    });

    describe("mobileAuthentication (2/3)", function () {
      describe("enroll", function () {
        it("Should succeed in enrolling an authenticated user", function (done) {
          onegini.mobileAuthentication.enroll(
              function () {
                expect(true).toBe(true);
                done();
              },
              function (err) {
                expect(err).toBeUndefined();
                fail("Error callback was called, but method should have succeeded");
              });
        }, 20000);
      });

      describe("on", function () {
        it('Should exist', function () {
          expect(onegini.mobileAuthentication.on).toBeDefined();
        });

        it('Should accept a mobile confirmation request', function (done) {
          onegini.mobileAuthentication.on("confirmation")
              .shouldAccept(function (request, accept, reject) {
                expect(request.type).toBeDefined();
                expect(request.message).toBeDefined();
                expect(request.profileId).toBeDefined();
                accept();
              })
              .catch(function () {
                fail("Mobile authentication request failed, but should have succeeded");
              })
              .success(function () {
                done();
              });

          sendMobileAuthenticationRequest();
        }, 10000);

        it('Should reject a mobile confirmation request', function (done) {
          onegini.mobileAuthentication.on("confirmation")
              .shouldAccept(function (request, accept, reject) {
                expect(request.type).toBeDefined();
                expect(request.message).toBeDefined();
                expect(request.profileId).toBeDefined();
                reject();
              })
              .catch(function () {
                done();
              })
              .success(function () {
                fail("Mobile authentication request succeeded, but should have failed");
              });

          sendMobileAuthenticationRequest();
        }, 10000);

        it('Should be able to handle multiple requests', function (done) {
          var counter = 0;

          onegini.mobileAuthentication.on("confirmation")
              .shouldAccept(function (request, accept, reject) {
                expect(request.type).toBeDefined();
                expect(request.message).toBeDefined();
                expect(request.profileId).toBeDefined();
                accept();
              })
              .catch(function () {
                fail('Mobile authentication request failed, but should have succeeded');
              })
              .success(function () {
                counter++;
                if (counter === 2) {
                  done();
                }
              });

          sendMobileAuthenticationRequest();
          sendMobileAuthenticationRequest();
        }, 10000);

        it('Should accept a mobile pin request', function (done) {
          onegini.mobileAuthentication.on("pin")
              .providePin(function (request, accept, reject) {
                expect(request.type).toBeDefined();
                expect(request.message).toBeDefined();
                expect(request.profileId).toBeDefined();
                expect(request.maxFailureCount).toBeDefined();
                expect(request.remainingFailureCount).toBeDefined();

                if (request.remainingFailureCount === request.maxFailureCount - 1) {
                  accept(pin);
                }
                else {
                  accept('invalid');
                }
              })
              .catch(function () {
                fail('Mobile authentication request failed, but should have succeeded');
              })
              .success(function () {
                done();
              });

          sendMobileAuthenticationRequest("push_with_pin");
        }, 10000);

        it('Should reject a mobile pin request', function (done) {
          onegini.mobileAuthentication.on("pin")
              .providePin(function (request, accept, reject) {
                expect(request.type).toBeDefined();
                expect(request.message).toBeDefined();
                expect(request.profileId).toBeDefined();
                expect(request.maxFailureCount).toBeDefined();
                expect(request.remainingFailureCount).toBeDefined();
                reject();
              })
              .catch(function () {
                done();
              })
              .success(function () {
                fail('Mobile authentication request succeeded, but should have failed');
              });

          sendMobileAuthenticationRequest("push_with_pin");
        }, 10000);
      });
    });

    describe('reauthenticate', function () {
      it('should exist', function () {
        expect(onegini.user.reauthenticate).toBeDefined();
      });

      it("should require a profileId", function () {
        expect(function () {
          onegini.user.reauthenticate()
        }).toThrow(new TypeError("Onegini: missing 'profileId' argument for reauthenticate"));
      });

      it("should succeed", function (done) {
        onegini.user.reauthenticate({profileId: registeredProfileId})
            .onPinRequest(function (actions) {
              expect(actions).toBeDefined();
              actions.providePin(pin);
            })
            .onError(function (err) {
              expect(err).toBeDefined();
              fail('Error callback called, but method should have succeeded');
            })
            .onSuccess(function () {
              done();
            });
      });
    });

    describe("authenticators (2/2)", function () {
      describe("setPreferred", function () {
        it("Should fail with a non-existing authenticator", function (done) {
          onegini.user.authenticators.setPreferred({
                authenticatorId: "invalid"
              }, function () {
                expect(true).toBe(true);
                fail("Success callback called, but method should have failed.");
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No such authenticator found");
                done();
              });
        });
      });

      describe("deregister", function() {
        it("Should fail with a non-existing authenticator", function (done) {
          onegini.user.authenticators.deregister(
              {
                authenticatorId: "invalid"
              },
              function (result) {
                expect(result).toBeUndefined();
              },
              function (err) {
                expect(err).toBeDefined();
                expect(err.description).toBe("Onegini: No such authenticator found");
                done();
              });
        });
      });

      describe("getAll", function () {
        it("should contain PIN and fingerprint authenticator (if available)", function (done) {
          var foundPin = false,
              foundFingerprint = false,
              shouldFindPin = true,
              shouldFindFingerprint = config.testForMultipleAuthenticators;


          onegini.user.authenticators.getAll(
              function (result) {
                expect(result).toBeDefined();

                for (var r in result) {
                  var authenticator = result[r];
                  expect(authenticator.authenticatorId).toBeDefined();
                  if (authenticator.authenticatorId === "com.onegini.authenticator.PIN") {
                    foundPin = true;
                  }
                  else if (authenticator.authenticatorId === config.fingerPrintAuthenticatorID) {
                    foundFingerprint = true;
                  }
                }

                expect(foundPin).toBe(shouldFindPin);
                expect(foundFingerprint).toBe(shouldFindFingerprint);
                done();
              },
              function (err) {
                expect(err).toBeUndefined();
                fail("Method failed, but should have succeeded");
              }
          );
        });
      });

      describe('getRegistered', function () {
        it("should contain a PIN authenticator", function (done) {
          onegini.user.authenticators.getRegistered(
              function (result) {
                expect(result).toBeDefined();
                var nrOfAuthenticators = result.length;
                expect(nrOfAuthenticators).toBeGreaterThan(0);

                for (var r in result) {
                  var authenticator = result[r];
                  expect(authenticator.authenticatorId).toBeDefined();
                  if (authenticator.authenticatorId === "com.onegini.authenticator.PIN") {
                    done();
                    return;
                  }
                }
                fail("Expected PIN Authenticator not found");
                done();
              },
              function (err) {
                expect(err).toBeUndefined();
              });
        });
      });

      describe('getNotRegistered', function () {
        it("should succeed", function (done) {
          onegini.user.authenticators.getNotRegistered(
              function (result) {
                expect(result).toBeDefined();
                done();
              },
              function (err) {
                expect(err).toBeUndefined();
              });
        });
      });

      describe("getPreferred", function () {
        it("Should succeed and be default PIN authenticator", function (done) {
          onegini.user.authenticators.getPreferred(
              function (result) {
                expect(result).toBeDefined();
                expect(result.authenticatorId).toBe("com.onegini.authenticator.PIN");
                done();
              },
              function (err) {
                expect(err).toBeUndefined();
                fail("Error callback called, but method should have succeeded");
              });
        });
      });

      if (config.testForMultipleAuthenticators) {
        describe("registerNew", function () {
          it("should succeed", function (done) {
            onegini.user.authenticators.registerNew({authenticatorId: config.fingerPrintAuthenticatorID})
                .onPinRequest(function (actions) {
                  actions.providePin(pin);
                })
                .onSuccess(function () {
                  expect(true).toBe(true);
                  done();
                })
                .onError(function (err) {
                  expect(err).toBeUndefined();
                  fail('Authenticator registration failed, but should have suceeded');
                });
          });
        });

        describe("setPreferred", function () {
          it("Should succeed with an existing authenticator", function (done) {
            onegini.user.authenticators.setPreferred(
                {
                  authenticatorId: "com.onegini.authenticator.PIN" //config.fingerPrintAuthenticatorID
                }, function () {
                  expect(true).toBe(true);
                  done();
                },
                function (err) {
                  expect(err).toBeUndefined();
                  fail("Error callback called, but method should have failed.");
                });
          });
        });

        describe("deregister", function () {
          it("Should succeed with existing fingerprint authenticator", function (done) {
            onegini.user.authenticators.deregister(
                {
                  authenticatorId: config.fingerPrintAuthenticatorID
                }, function () {
                  expect(true).toBe(true);
                  done();
                }, function (err) {
                  expect(err).toBeUndefined();
                  fail("Error callback called, but method should have failed.");
                });
          });
        });
      }
      else {
        console.warn("Skipping authenticators (2/2). Multiple authenticator tests disabled");
      }
    });

    if (config.testForMobileFingerprintAuthentication) {
      describe("mobileAuthentication (3/3)", function () {
        describe("on", function () {
          it("Should accept a mobile fingerprint request", function (done) {
            onegini.mobileAuthentication.on("fingerprint")
                .shouldAccept(function (request, accept, reject) {
                  console.log("Please provide correct fingerprint");
                  expect(request.type).toBeDefined();
                  expect(request.message).toBeDefined();
                  expect(request.profileId).toBeDefined();
                  accept();
                })
                .catch(function () {
                  fail("Mobile authentication request failed, but should have succeeded");
                })
                .success(function () {
                  done();
                });

            sendMobileAuthenticationRequest("push_with_fingerprint");
          }, 10000);

          it("Should reject a mobile fingerprint request", function (done) {
            onegini.mobileAuthentication.on("fingerprint")
                .shouldAccept(function (request, accept, reject) {
                  expect(request.type).toBeDefined();
                  expect(request.message).toBeDefined();
                  expect(request.profileId).toBeDefined();
                  reject();
                })
                .catch(function () {
                  done();
                })
                .success(function () {
                  fail("Mobile authentication request succeeded, but should have failed");
                });

            sendMobileAuthenticationRequest("push_with_fingerprint");
          }, 10000);

          if (config.platform == "android") {
            it("Should be notified on fingerprint captured", function (done) {
              var didCallCaptured = false;
              onegini.mobileAuthentication.on("fingerprint")
                  .shouldAccept(function (request, accept, reject) {
                    console.log("Please provide any fingerprint");
                    accept();
                  })
                  .onFingerprintCaptured(function () {
                    console.log("Please remove your finger");
                    didCallCaptured = true;
                  })
                  .catch(function () {
                    fail("Mobile fingerprint authentication threw instead of triggering fingerprint capture event");
                  })
                  .success(function () {
                    expect(didCallCaptured).toBe(true);
                    setTimeout(done, 500);
                  });

              sendMobileAuthenticationRequest("push_with_fingerprint");
            }, 10000);

            it("Should request fingerprint authentication again on incorrect fingerprint", function (done) {
              onegini.mobileAuthentication.on("fingerprint")
                  .shouldAccept(function (request, accept, reject) {
                    console.log("Please provide incorrect fingerprint");
                    accept();
                  })
                  .onFingerprintNextAttempt(function () {
                    expect(true).toBe(true);
                    done();
                  })
                  .catch(function () {
                    fail("Mobile fingerprint authentication threw instead of requesting another attempt");
                  })
                  .success(function () {
                    fail("Mobile fingerprint authentication didn't request another attempt (or you supplied a correct fingerprint)")
                  });

              sendMobileAuthenticationRequest("push_with_fingerprint");
            }, 10000);
          }
        });
      });
    }
    else {
      console.warn("Skipping mobileAuthentication (3/3). Mobile fingerprint authentication tests disabled");
    }
  });

  /******** onegini.resource (1/2) *********/

  describe('onegini.resource', function () {
    it('should exist', function () {
      expect(onegini.resource).toBeDefined();
    });

    describe('fetch', function () {
      it('should exist', function () {
        expect(onegini.resource.fetch).toBeDefined();
      });

      it('should fetch a non-anonymous resource', function (done) {
        onegini.resource.fetch(
            {
              url: 'https://demo-msp.onegini.com/resources/devices',
              headers: {
                'x-test-string': 'foobar',
                'x-test-int': 1337
              }
            },
            function (response) {
              expect(response).toBeDefined();
              var body = response.body;
              expect(body).toBeDefined();
              expect(JSON.parse(body).devices).toBeDefined();
              expect(response.headers).toBeDefined();
              expect(response.status).toEqual(200);
              expect(response.statusText).toBeDefined();
              done();
            }, function (err) {
              expect(err).toBeUndefined();
              fail('Error callback called, but method should have succeeded');
            });
      });

      it("should require a url", function () {
        expect(function () {
          onegini.resource.fetch();
        }).toThrow(new TypeError("Onegini: missing 'url' argument for fetch"));
      });

      it('should return error context when request fails', function (done) {
        onegini.resource.fetch({
              method: 'POST',
              url: 'https://demo-msp.onegini.com/resources/devices'
            }, function (response) {
              expect(response).toBeUndefined();
              fail('Success callback called, but method should have failed');
            },
            function (response) {
              expect(response).toBeDefined();
              expect(response.status).toEqual(405);
              done();
            })
      });

      it('should intercept an XMLHttpRequest', function (done) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'https://demo-msp.onegini.com/resources/devices');
        xhr.onload = function () {
          expect(this.readyState).toEqual(4);
          expect(this.status).toBe(200);
          expect(JSON.parse(this.responseText).devices).toBeDefined();
          done();
        };
        xhr.send();
      });
    });
  });

  /******** onegini.user (2/2) *********/

  describe('onegini.user', function () {
    describe('changePin', function () {
      it("Should exist", function () {
        expect(onegini.user.changePin).toBeDefined();
      });

      it("Should succeed", function (done) {
        var count = 0;

        onegini.user.changePin()
            .onPinRequest(function (actions, options) {
              expect(actions).toBeDefined();
              expect(actions.providePin).toBeDefined();
              expect(options).toBeDefined();
              actions.providePin(pin);
            })
            .onCreatePinRequest(function (actions, options) {
              expect(options.pinLength).toBe(5);
              expect(function () {
                actions.createPin();
              }).toThrow(new TypeError('Onegini: missing "pin" argument for createPin'));

              if (count === 0) {
                actions.createPin('invalid');
              }
              else {
                expect(count).toBe(1);
                actions.createPin(pin);
              }

              count += 1;
            })
            .onSuccess(function () {
              done();
            })
            .onError(function (err) {
              expect(err).toBeUndefined();
              fail('Change pin failed, but should have succeeded');
            });
      });
    });

    describe('isUserRegistered', function () {
      it("should exist", function () {
        expect(onegini.user.isUserRegistered).toBeDefined();
      });

      it("'profileId' argument mandatory", function () {
        expect(function () {
          onegini.user.isUserRegistered({}, function () {
          }, function () {
          });
        }).toThrow(new TypeError("Onegini: missing 'profileId' argument for isUserRegistered"));
      });

      it("should succeed with correct profileId", function (done) {
        onegini.user.isUserRegistered(
            {
              profileId: registeredProfileId
            },
            function (result) {
              expect(result).toBe(true);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });

      it("should fail with incorrect profileId", function (done) {
        onegini.user.isUserRegistered(
            {
              profileId: "UNKNOWN"
            },
            function (result) {
              expect(result).toBe(false);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

    describe('deregister', function () {
      it("should exist", function () {
        expect(onegini.user.deregister).toBeDefined();
      });

      it("'profileId' argument mandatory", function () {
        expect(function () {
          onegini.user.deregister({}, function () {
          }, function () {
          });
        }).toThrow(new TypeError("Onegini: missing 'profileId' argument for deregister"));
      });

      it("no user found for profileId", function (done) {
        onegini.user.deregister(
            {
              profileId: "UNKNOWN"
            },
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("Onegini: No registered user found.");
              done();
            });
      });

      it("should succeed with correct profileId", function (done) {
        onegini.user.deregister(
            {
              profileId: registeredProfileId
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

    describe("getAuthenticatedUserProfile (3/3)", function () {
      it("should fail again", function (done) {
        onegini.user.getAuthenticatedUserProfile(
            function (result) {
              expect(result).toBeUndefined();
            },
            function (err) {
              expect(err).toBeDefined();
              expect(err.description).toBe("Onegini: No user authenticated.");
              done();
            });
      });
    });

    describe('getUserProfiles (2/2)', function () {
      it("should be one less", function (done) {
        onegini.user.getUserProfiles(
            function (result) {
              expect(result).toBeDefined();
              expect(result.length).toBeLessThan(nrOfUserProfiles);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });

  });


  /******** onegini.device *********/

  describe('onegini.device', function () {
    it("should exist", function () {
      expect(onegini.device).toBeDefined();
    });

    describe("authenticate", function () {
      it("should exist", function () {
        expect(onegini.device.authenticate).toBeDefined();
      });

      it('should fail with invalid scopes', function (done) {
        onegini.device.authenticate(
            {
              scopes: ["incorrect"]
            },
            function () {
              expect(true).toBe(false);
            },
            function (err) {
              expect(err).toBeDefined();
              done();
            });
      });

      it('should succeed with valid scopes', function (done) {
        onegini.device.authenticate(
            {
              scopes: ["application-details"]
            },
            function () {
              expect(true).toBe(true);
              done();
            },
            function (err) {
              expect(err).toBeUndefined();
            });
      });
    });
  });

  /******** onegini.resource (2/2) *********/

  describe('onegini.resource', function () {
    it('should fetch an anonymous resource', function (done) {
      onegini.resource.fetch({
            url: 'https://demo-msp.onegini.com/resources/application-details',
            anonymous: true
          },
          function (response) {
            expect(response).toBeDefined();
            expect(response.body).toBeDefined();
            expect(response.headers).toBeDefined();
            expect(response.status).toEqual(200);
            expect(response.statusText).toBeDefined();
            done();
          },
          function (errResponse) {
            expect(errResponse).toBeUndefined();
            fail('Error response called, but method should have succeeded');
          })
    })
  });
};