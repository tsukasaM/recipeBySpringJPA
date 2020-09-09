"use strict";
 
const expect = require("chai").expect;
const yaml = require('js-yaml');
const fs = require('fs');
const validUrl = require("valid-url");
 
let chai = require('chai');
const chaiHttp = require('chai-http');
chai.use(chaiHttp);
 
let env;
try {
   env = yaml.safeLoad(fs.readFileSync('env.yml', 'utf8'));
} catch (e) {
   console.log(e);
}
 
let BASE_URL = env.BASE_URL;
 
 
describe("codecheck.yml BASE_URL", () => {
 it("contains a valid URL", () => {
   console.log(BASE_URL);
   // "記述されたURLが無効な形式です。"
   expect(validUrl.isUri(BASE_URL)).to.be.ok;
 });
});
 
describe("API server", () => {
 it(`returns status code 200 when accessing ${BASE_URL}/`, function(done) {
    this.timeout(6000);
   chai.request(BASE_URL)
   .get("/")
   .catch(function (res) {
     expect(res.status).to.equal(404);
     done();
   });
 });
});
