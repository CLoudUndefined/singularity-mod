{
  description = "Minecraft Forge 1.20.1 (47.4.0) MDK";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs =
    { nixpkgs, ... }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
      jdk = pkgs.jdk17;
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = [
          jdk
          pkgs.mesa
          pkgs.libGL
          pkgs.xorg.libX11
          pkgs.xorg.libXrandr
          pkgs.xorg.libXext
          pkgs.xorg.libXrender
          pkgs.libGLU
          pkgs.glfw
          pkgs.mesa-demos
          pkgs.openal
          pkgs.libpulseaudio
          pkgs.alsa-lib
        ];

        shellHook = ''
          export JAVA_HOME=${jdk}
          export LD_LIBRARY_PATH=${
            pkgs.lib.makeLibraryPath [
              pkgs.mesa
              pkgs.libGL
              pkgs.xorg.libX11
              pkgs.xorg.libXrandr
              pkgs.xorg.libXext
              pkgs.xorg.libXrender
              pkgs.libGLU
              pkgs.glfw
            ]
          }:$LD_LIBRARY_PATH
          echo "Forge 1.20.1 MDK dev environment"
          echo "Java version: $(${jdk}/bin/java -version)"
        '';
      };
    };
}
