// Remplacez 'votre_clé_secrète' par une clé secrète valide, comme un mot de passe fort ou une variable d'environnement
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const User = require('../models/User');

const register = async (req, res) => {
  const { name, lastName, email, password, age } = req.body;

  const existingUser = await User.findOne({ email });
  if (existingUser) {
    return res.status(400).json({ message: 'L\'utilisateur existe déjà' });
  }

  const hashedPassword = await bcrypt.hash(password, 10);

  const newUser = new User({
    name,
    lastName,
    email,
    password: hashedPassword,
    age
  });

  try {
    const savedUser = await newUser.save();
    res.status(201).json({ message: 'Utilisateur créé avec succès', user: savedUser });
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
};

const login = async (req, res) => {
  const { email, password } = req.body;

  const user = await User.findOne({ email });
  if (!user) {
    return res.status(400).json({ message: 'Utilisateur non trouvé' });
  }

  const isMatch = await bcrypt.compare(password, user.password);
  if (!isMatch) {
    return res.status(400).json({ message: 'Mot de passe incorrect' });
  }

  // Utilisez une clé secrète dans la signature du token
  const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET || 'votre_clé_secrète', { expiresIn: '1h' });

  res.json({ message: 'Connexion réussie', token });
};

module.exports = { register, login };
