"use strict";
 
const expect = require("chai").expect;
const yaml = require('js-yaml');
const fs = require('fs');
 
let chai = require('chai');
const chaiHttp = require('chai-http');
chai.use(chaiHttp);
let should = chai.should();
 
let env;
try {
   env = yaml.safeLoad(fs.readFileSync('env.yml', 'utf8'));
} catch (e) {
   console.log(e);
}
 
let BASE_URL = env.BASE_URL;
let POST_RECIPE_PATH    = "/recipes";
let GET_ALL_RECIPE_PATH = "/recipes";
let GET_ONE_RECIPE_PATH = "/recipes/";
let UPDATE_RECIPE_PATH  = "/recipes/";
let DELETE_RECIPE_PATH  = "/recipes/";
 
// BEGIN_CHALLENGE
// BASE_URL = env.BASE_URL_TEST;
// POST_RECIPE_PATH = env.POST_TEST;
// GET_ALL_RECIPE_PATH = env.GET_TEST;
// GET_ONE_RECIPE_PATH = env.GET_ONE_TEST;
// UPDATE_RECIPE_PATH = env.UPDATE_TEST;
// DELETE_RECIPE_PATH = env.DELETE_TEST;
// END_CHALLENGE
 
let recipeIdList = [];
 
describe('/GET recipes', () => {
 it('should GET all the recipes', (done) => {
   chai.request(BASE_URL)
   .get(GET_ALL_RECIPE_PATH)
   .end((err, res) => {
     res.should.have.status(200);
     res.body.should.be.a('object');
     for (let i=0; i < res.body.recipes.length; i++) {
       recipeIdList.push(res.body.recipes[i].id);
     }
     done();
   });
 });
});
 
describe('/POST recipes', (done) => {
 it('should not POST recipe without title, making_time, serves, ingredients, cost', (done) => {
   let recipe = {
   }
   chai.request(BASE_URL)
   .post(POST_RECIPE_PATH)
   .send(recipe)
   .end((err, res) => {
     res.should.have.status(200);
     res.body.should.be.a('object');
     res.body.should.have.property('message').eql('Recipe creation failed!');
     res.body.should.have.property('required').eql('title, making_time, serves, ingredients, cost');
     done();
   });
 });
 it('should POST a recipe ', (done) => {
   let recipe = {
     "title": "トマトスープ",
     "making_time": "15分",
     "serves": "5人",
     "ingredients": "玉ねぎ, トマト, スパイス, 水",
     "cost": 450
   }
   chai.request(BASE_URL)
   .post(POST_RECIPE_PATH)
   .send(recipe)
   .end((err, res) => {
     res.should.have.status(200);
     res.body.should.be.a('object');
     res.body.should.have.property('message').eql('Recipe successfully created!');
     res.body.recipe.should.be.a('array');
     res.body.recipe[0].should.have.property('title');
     res.body.recipe[0].should.have.property('making_time');
     res.body.recipe[0].should.have.property('serves');
     res.body.recipe[0].should.have.property('ingredients');
     res.body.recipe[0].should.have.property('cost');
     done();
   });
 });
});
 
describe('/GET/{id} recipe', () => {
 it('should GET a recipe by the given id', (done) => {
   chai.request(BASE_URL)
   .get(GET_ONE_RECIPE_PATH + recipeIdList[0])
   .end((err, res) => {
     res.should.have.status(200);
     res.body.should.be.a('object');
     res.body.should.have.property('message').eql('Recipe details by id');
     res.body.recipe.should.be.a('array');
     res.body.recipe[0].should.have.property('title');
     res.body.recipe[0].should.have.property('making_time');
     res.body.recipe[0].should.have.property('serves');
     res.body.recipe[0].should.have.property('ingredients');
     res.body.recipe[0].should.have.property('cost');
     done();
   });
 });
});
 
describe('/PATCH/{id} recipe', () => {
 it('should UPDATE a recipe ', (done) => {
   let recipe = {
     "title": "オムライス",
     "making_time": "20分",
     "serves": "7人",
     "ingredients": "玉ねぎ,卵,スパイス,醤油",
     "cost": 400
   }
   chai.request(BASE_URL)
   .patch(UPDATE_RECIPE_PATH + recipeIdList[0])
   .send(recipe)
   .end((err, res) => {
     res.should.have.status(200);
     res.body.should.be.a('object');
     res.body.should.have.property('message').eql('Recipe successfully updated!');
     res.body.recipe.should.be.a('array');
     res.body.recipe[0].should.have.property('title');
     res.body.recipe[0].should.have.property('making_time');
     res.body.recipe[0].should.have.property('serves');
     res.body.recipe[0].should.have.property('ingredients');
     res.body.recipe[0].should.have.property('cost');
     done();
   });
 });
});
 
describe('/DELETE/{id} recipe', () => {
 it('should DELETE a recipe ', (done) => {
   chai.request(BASE_URL)
   .delete(DELETE_RECIPE_PATH + recipeIdList[0])
   .end((err, res) => {
     res.should.have.status(200);
     res.body.should.be.a('object');
     res.body.should.have.property('message').eql('Recipe successfully removed!');
     done();
   });
 });
});
