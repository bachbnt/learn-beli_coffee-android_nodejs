const mongoose = require('mongoose');

const ProductSchema = mongoose.Schema({
    title: String,
    content: String,
    image:String
});

module.exports = mongoose.model('Products', ProductSchema);