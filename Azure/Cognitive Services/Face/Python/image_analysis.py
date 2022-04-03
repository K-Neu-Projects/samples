import azure_face
from os import walk, path
from config import images_directory

def get_images():
    images = []

    for (dirpath, dirnames, filenames) in walk(images_directory):
        for (n) in filenames:
            ext = path.splitext(n)[-1].lower()
            if ext == ".jpeg" or ext == ".jpg" or ext == ".png" or ext == ".gif" or ext == ".bmp":
                images.append(dirpath + n)
        break

    return images


def analyse_images():

    face_results = []

    for(image) in get_images():
        result = azure_face.call_face(image)
        face_results.append(result)
    
    return face_results

analyse_images()