
Pod::Spec.new do |s|
  s.name         = "RNAccessMediaLocationPermission"
  s.version      = "1.0.0"
  s.summary      = "RNAccessMediaLocationPermission"
  s.description  = <<-DESC
                  RNAccessMediaLocationPermission is used to enable the media location permission
                   DESC
  s.homepage     = "https://github.com/chaitanya55drk/react-native-media-location-permission"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "Venkata Chaithanya" => "vchaitanya@travogram.com" }
  s.platform     = :ios, "10.0"
  s.source       = { :git => "https://github.com/chaitanya55drk/react-native-media-location-permission.git", :tag => "master" }
  s.source_files  = "RNAccessMediaLocationPermission/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  