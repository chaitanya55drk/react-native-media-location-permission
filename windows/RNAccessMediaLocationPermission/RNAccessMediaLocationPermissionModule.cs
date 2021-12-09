using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Access.Media.Location.Permission.RNAccessMediaLocationPermission
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNAccessMediaLocationPermissionModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNAccessMediaLocationPermissionModule"/>.
        /// </summary>
        internal RNAccessMediaLocationPermissionModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNAccessMediaLocationPermission";
            }
        }
    }
}
