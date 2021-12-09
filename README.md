# react-native-media-location-permission
Android exif interface location of media in sdk above 29 can be accessible by just calling this module.

```javascript
import RNAccessMediaLocationPermission from 'react-native-media-location-permission';

...

RNAccessMediaLocationPermission.requestMediaLocationPermission().then(result => {
  ...
  //Result == granted or blocked
}).catche(exception => {
  ...
  //Error while fetching
})

```
