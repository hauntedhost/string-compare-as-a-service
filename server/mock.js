const _ = require('lodash/fp');
const express = require('express');
const cors = require('cors');
const simulateLatency = require('express-simulate-latency');

const app = express();

app.use(cors());
app.use(simulateLatency({ min: 300, max: 1500 }));

const port = 9000;
const requiredKeys = ['a', 'b'];

const isValid = req => {
  return (
    // all requireKeys are present
    _.pipe([
      _.pick(requiredKeys),
      _.keys,
      _.difference(requiredKeys),
      _.isEmpty
    ])(req.query) &&
    // all requireKeys are non-blank string
    _.pipe([
      _.pick(requiredKeys),
      _.values,
      _.every(s => _.isString(s) && !!_.trim(s))
    ])(req.query)
  );
};

app.get('/compare', function(req, res) {
  console.log('compare params:', req.query);
  if (isValid(req)) {
    res.json({ areEqual: req.query.a === req.query.b });
  } else {
    res.status(422);
    res.json({ error: `request must include params: ${requiredKeys}` });
  }
});

app.listen(port, () => console.log(`listening on port ${port}`));
