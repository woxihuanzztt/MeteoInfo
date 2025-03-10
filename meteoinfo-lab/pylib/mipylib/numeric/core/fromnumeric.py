# coding=utf-8

from .numeric import asarray, array
from ._ndarray import NDArray
from org.meteoinfo.ndarray.math import ArrayUtil

__all__ = ['ndim','nonzero','ravel','searchsorted']

def ndim(a):
    """
    Return the number of dimensions of an array.
    Parameters
    ----------
    a : array_like
        Input array.  If it is not already an ndarray, a conversion is
        attempted.
    Returns
    -------
    number_of_dimensions : int
        The number of dimensions in `a`.  Scalars are zero-dimensional.
    See Also
    --------
    ndarray.ndim : equivalent method
    shape : dimensions of array
    ndarray.shape : dimensions of array
    Examples
    --------
    >>> np.ndim([[1,2,3],[4,5,6]])
    2
    >>> np.ndim(np.array([[1,2,3],[4,5,6]]))
    2
    >>> np.ndim(1)
    0
    """
    try:
        return a.ndim
    except AttributeError:
        return asarray(a).ndim

def ravel(a):
    '''
    Return a contiguous flattened array.

    :param a: (*array*) Input array.
    :return: A contiguous flattened array.
    '''
    if isinstance(a, (list, tuple)):
        a = array(a)

    return a.ravel()

def nonzero(a):
    '''
    Return the indices of the elements that are non-zero.

    Returns a tuple of arrays, one for each dimension of a, containing the indices of the
    non-zero elements in that dimension.

    :param a: (*array_like*) Input array.

    :returns: (*tuple*) Indices of elements that are non-zero.
    '''
    if isinstance(a, list):
        a = array(a)
    ra = ArrayUtil.nonzero(a.asarray())
    if ra is None:
        return None

    r = []
    for aa in ra:
        r.append(NDArray(aa))
    return tuple(r)

def searchsorted(a, v, side='left', sorter=None):
    """
    Find indices where elements should be inserted to maintain order.
    :param a: (*array_like*) Input 1-D array. If sorter is None, then it must be sorted in ascending order,
        otherwise sorter must be an array of indices that sort it.
    :param v: (*array_like*) Values to insert into a.
    :param side: (*str*) [left | right], default is `left`. If `left`, the index of the first suitable location found is given.
        If `right`, return the last such index. If there is no suitable index, return either 0 or N (where N
        is the length of a).
    :param sorter: (*array_like*) Optional array of integer indices that sort array a into ascending order.
        They are typically the result of argsort.
    :return: (*array_like*) Array of insertion points with the same shape as v.
    """
    if isinstance(a, (list, tuple)):
        a = array(a).asarray()
    elif isinstance(a, NDArray):
        a = a.asarray()

    if isinstance(v, (list, tuple)):
        v = array(v).asarray()
    elif isinstance(v, NDArray):
        v = v.asarray()

    left = True if side == 'left' else False
    r = ArrayUtil.searchSorted(a, v, left)
    if isinstance(r, int):
        return r
    else:
        return NDArray(r)