var glCanvas = document.getElementById("glcanvas");

var scene = new THREE.Scene(
    {
        color: "white"
    }
);

var camera = new THREE.PerspectiveCamera(
    100, 
    window.innerWidth / window.innerHeight, 
    1, 
    1000
);

var renderer = new THREE.WebGLRenderer(
    {
        antialias: true, 
        alpha: true
    }
);

renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

function animate() {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
}

//---------------- LIGHTS ---------------------------------------------------
var light_directional = new THREE.DirectionalLight(0xffffff, 1);
light_directional.position.set(-10, 10, 10);
camera.add(light_directional);
scene.add(camera);

//----------------- COLORS --------------------------------------------------
shape_color = new THREE.MeshPhongMaterial({color: "white"});

//-----------------GROUP 1---------------------------------------------------
base_BOTTOM = new THREE.Mesh(
    new THREE.CylinderGeometry(1, 1, 0.3, 100),
    shape_color
);

base_TOP = new THREE.Mesh(
    new THREE.CylinderGeometry(0.7, 1, 0.4, 100),
    shape_color
);
base_TOP.position.set(0, 0.35, 0);

GROUP_BASE = new THREE.Group();
GROUP_BASE.add(base_BOTTOM);
GROUP_BASE.add(base_TOP);

GROUP_BASE.position.set(0, -3, 0);
GROUP_BASE.scale.set(3,2,2);

//---------------- GROUP 2 --------------------------------------------------
middle_BOTTOM = new THREE.Mesh(
    new THREE.CylinderGeometry(0.60, 0.7, 0.5, 100),
    shape_color
);
middle_BOTTOM.position.set(0, -0.71, 0);

middle_MIDDLE = new THREE.Mesh(
    new THREE.CylinderGeometry(0.4, 0.6, 1.2, 100),
    shape_color
);
middle_MIDDLE.position.set(0, 0.14, 0);

middle_TOP = new THREE.Mesh(
    new THREE.CylinderGeometry(0.3, 0.4, 1, 100),
    shape_color
);
middle_TOP.position.set(0, 1.2, 0);

GROUP_MIDDLE = new THREE.Group();
GROUP_MIDDLE.add(middle_BOTTOM);
GROUP_MIDDLE.add(middle_MIDDLE);
GROUP_MIDDLE.add(middle_TOP);
GROUP_MIDDLE.scale.set(3, 2, 2);


//---------------- GROUP 3 --------------------------------------------------
middle_DETAILS_RING = new THREE.Mesh(
    new THREE.CylinderGeometry(0.58, 0.63, 0.3, 100),
    shape_color
);
middle_DETAILS_RING.position.set(0, 1.7, 0);
middle_DETAILS_RING.scale.set(1.1, 1.1, 1.1);

middle_DETAILS_RING2 = new THREE.Mesh(
    new THREE.CylinderGeometry(0.4, 0.58, 0.2, 100),
    shape_color
);
middle_DETAILS_RING2.position.set(0, 1.96, 0);
middle_DETAILS_RING2.scale.set(1.1, 1.1, 1.1);

GROUP_DETAILS = new THREE.Group();
GROUP_DETAILS.add(middle_DETAILS_RING);
GROUP_DETAILS.add(middle_DETAILS_RING2);

GROUP_DETAILS.scale.set(3,2,2);

//---------------- GROUP 4 --------------------------------------------------

top_BALL = new THREE.Mesh(
    new THREE.SphereGeometry(0.65, 50, 50),
    shape_color
);
top_BALL.position.set(0, 2.56, 0);

GROUP_TOP = new THREE.Group();
GROUP_TOP.add(top_BALL);
GROUP_TOP.scale.set(2, 2, 2);

//------------- FULL-SHAPE --------------------------------------------------
PAWN_SHAPE = new THREE.Group();

PAWN_SHAPE.add(GROUP_BASE);
PAWN_SHAPE.add(GROUP_MIDDLE);
PAWN_SHAPE.add(GROUP_DETAILS);
PAWN_SHAPE.add(GROUP_TOP);

PAWN_SHAPE.scale.set(0.3, 0.3, 0.3);

scene.add(PAWN_SHAPE);
camera.position.set(0, 0.3, 3);

animate();