from flask import jsonify


def iterateModel(models, keys=None):
    res = []
    if keys is None:
        for model in models:
            res.append(model.raw())
    else:
        for model in models:
            raw = model.raw()
            filtered = {}
            for k in keys:
                if k in raw.keys():
                    filtered[k] = raw[k]
            res.append(filtered)

    return res
